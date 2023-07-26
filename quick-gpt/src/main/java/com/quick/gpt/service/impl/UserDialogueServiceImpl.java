package com.quick.gpt.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quick.common.enums.ResultCode;
import com.quick.common.enums.Status;
import com.quick.common.exception.BusinessException;
import com.quick.gpt.dao.DialogueTopicMapper;
import com.quick.gpt.dao.UserDialogueMapper;
import com.quick.gpt.dao.UserProblemLogRepository;
import com.quick.gpt.domain.po.DialogueTopicPO;
import com.quick.gpt.domain.po.UserDialoguePO;
import com.quick.gpt.domain.po.UserProblemLogPO;
import com.quick.gpt.enums.ProblemType;
import com.quick.gpt.enums.chat.RoleEnum;
import com.quick.gpt.service.UserDialogueService;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserDialogueServiceImpl implements UserDialogueService {


    @Autowired
    private UserDialogueMapper userDialogueMapper;

    @Autowired
    private DialogueTopicMapper dialogueTopicMapper;

    @Autowired
    private UserProblemLogRepository userProblemLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<UserDialoguePO> getDialogueByUserId(Long userId, Boolean isTopic) {
        LambdaQueryWrapper<UserDialoguePO> query = new LambdaQueryWrapper<>();
        query.eq(UserDialoguePO::getUserId,userId).eq(UserDialoguePO::getStatus, Status.VALID);
        if(isTopic){
            query.isNotNull(UserDialoguePO::getTopicId);
        }else{
            query.isNull(UserDialoguePO::getTopicId);
        }
        List<UserDialoguePO> list = this.userDialogueMapper.selectList(query);
        //普通会话如果没有则默认新建一个
        if(!isTopic&&list.size()==0){
            UserDialoguePO userDialoguePO = new UserDialoguePO();
            userDialoguePO.setUserId(userId)
                    .setDescribe("新建会话")
                    .setName("新建会话");
            addDialogue(userDialoguePO);
            list.add(userDialoguePO);
        }
        //技能会话如果没有则默认新建一个
        if(isTopic&&list.size()==0){
            QueryWrapper<DialogueTopicPO> dialogueTopicPOQueryWrapper = new QueryWrapper<>();
            List<DialogueTopicPO> dialogueTopics = dialogueTopicMapper.selectList(dialogueTopicPOQueryWrapper);
            if(dialogueTopics.size()!=0){
                UserDialoguePO userDialoguePO = new UserDialoguePO();
                userDialoguePO.setUserId(userId)
                        .setDescribe(dialogueTopics.get(0).getDescribe())
                        .setName(dialogueTopics.get(0).getName())
                        .setTopicId(dialogueTopics.get(0).getId());
                addDialogue(userDialoguePO);
                list.add(userDialoguePO);
            }
        }
        return list;
    }

    @Override
    public List<UserDialoguePO> getDialogueByName(Long userId, String name,Boolean isTopic) {
        LambdaQueryWrapper<UserDialoguePO> query = new LambdaQueryWrapper<>();
        query.eq(UserDialoguePO::getUserId,userId)
                .eq(UserDialoguePO::getStatus, Status.VALID);
        if(!StringUtils.isEmpty(name)){
            query.like(UserDialoguePO::getName,name);
        }
        if(isTopic){
            query.isNotNull(UserDialoguePO::getTopicId);
        }else{
            query.isNull(UserDialoguePO::getTopicId);
        }
        return this.userDialogueMapper.selectList(query);
    }

    @Override
    public UserDialoguePO modifyDialogue(UserDialoguePO dialoguePO) {
        try {
            this.userDialogueMapper.updateById(dialoguePO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.MODIFY_DIALOG_FAIL);
        }
        return dialoguePO;
    }

    @Override
    public void deleteDialogueById(Long id) {
        try {
            userDialogueMapper.deleteById(id);
            Query query = new Query(Criteria.where("dialogueId").is(id));
            mongoTemplate.remove(query,"user_problem_log");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.REMOVE_DIALOG_FAIL);
        }
    }

    @Transactional
    @Override
    public UserDialoguePO addDialogue(UserDialoguePO dialoguePO) {
        String id = null;
        try {
            if(dialoguePO.getTopicId()!=null){
                LambdaQueryWrapper<UserDialoguePO> query = new LambdaQueryWrapper<>();
                query.eq(UserDialoguePO::getTopicId,dialoguePO.getTopicId())
                        .eq(UserDialoguePO::getStatus,Status.VALID)
                        .eq(UserDialoguePO::getUserId,dialoguePO.getUserId());
                List<UserDialoguePO> userDialoguePOS = userDialogueMapper.selectList(query);
                if(userDialoguePOS.size()!=0){
                    return userDialoguePOS.get(0);
                }
            }
            userDialogueMapper.insert(dialoguePO);
            if(dialoguePO.getTopicId()!=null){
                DialogueTopicPO topic = dialogueTopicMapper.selectById(dialoguePO.getTopicId());
                LocalDateTime now = LocalDateTime.now();
                ChatMessage userMsg = new ChatMessage(RoleEnum.USER.getRoleName(), topic.getTemplate());
                ChatMessage assistantMsg = new ChatMessage(RoleEnum.ASSISTANT.getRoleName(), "好的");
                //7.存储问答记录到mongodb
                UserProblemLogPO userProblemLogPO = new UserProblemLogPO();
                id = IdUtil.objectId();
                userProblemLogPO.setId(id)
                        .setUserId(dialoguePO.getUserId())
                        .setAnswer(assistantMsg.getContent())
                        .setQuestion(userMsg.getContent())
                        .setType(ProblemType.TEXT.getCode())
                        .setStatus(Status.VALID.getCode())
                        .setAskTime(now)
                        .setDialogueId(dialoguePO.getId())
                        .setTopic(true);
                userProblemLogRepository.insert(userProblemLogPO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(id!=null){
                Query query = new Query(Criteria.where("id").is(id));
                mongoTemplate.remove(query,"user_problem_log");
            }
            throw new BusinessException(ResultCode.ADD_DIALOG_FAIL);
        }
        return dialoguePO;
    }
}
