package com.quick.member.service.impl;

import cn.hutool.core.util.IdUtil;
import com.quick.member.common.config.exception.BusinessException;
import com.quick.member.common.config.params.ChatGptParamsConfig;
import com.quick.member.common.config.params.ServiceParamsConfig;
import com.quick.member.common.enums.*;
import com.quick.member.domain.po.UseAccountPO;
import com.quick.member.domain.po.UseLogPO;
import com.quick.member.domain.po.UserMemberPO;
import com.quick.member.mongodb.po.UserProblemLogPO;
import com.quick.member.mongodb.repository.UserProblemLogRepository;
import com.quick.member.service.UseAccountService;
import com.quick.member.service.UseLogService;
import com.quick.member.service.UserMemberService;
import com.quick.member.service.UserProblemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserProblemLogServiceImpl implements UserProblemLogService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserProblemLogRepository userProblemLogRepository;

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private UseAccountService useAccountService;

    @Autowired
    private UseLogService useLogService;

    @Autowired
    private ChatGptParamsConfig config;

    @Override
    public List<UserProblemLogPO> queryAllByUserId(Long userId, ProblemType type) {
        Query query = new Query();
        Criteria criteria = Criteria.where("status").is(Status.VALID.getCode())
                .and("userId").is(userId)
                .and("type").is(type.getCode());
        query.addCriteria(criteria);
        query.with(Sort.by(Sort.Direction.ASC, "askTime"));
        return mongoTemplate.find(query, UserProblemLogPO.class);
    }

    @Override
    public List<UserProblemLogPO> queryAllByUserIdAndDiaId(Long userId,Long dialogId, ProblemType type) {
        Query query = new Query();
        Criteria criteria = Criteria.where("status").is(Status.VALID.getCode())
                .and("userId").is(userId)
                .and("type").is(type.getCode())
                .and("dialogueId").is(dialogId);
        query.addCriteria(criteria);
        query.with(Sort.by(Sort.Direction.ASC, "askTime"));
        return mongoTemplate.find(query, UserProblemLogPO.class);
    }

    @Transactional
    @Override
    public UserProblemLogPO saveAndUpdate(Long dialogId,Boolean isUpper,UserMemberPO userMemberPO, UseAccountPO useAccountPO, String answer, String question, boolean isMember) {
        String id = IdUtil.objectId();
        try {
            LocalDateTime now = LocalDateTime.now();
            if(userMemberPO!=null&& isMember){
                //5.如果是会员，会员今日剩余使用减少
                userMemberPO.setBalanceCountTime(LocalDateTime.now());
                userMemberPO.setTodayBalanceCount(userMemberPO.getTodayBalanceCount() + 1);
                userMemberService.updateById(userMemberPO);
            }else{
                //6.如果不是会员，更新次数账户
                useAccountPO.setBalanceCount(useAccountPO.getBalanceCount() - 1);
                useAccountService.updateById(useAccountPO);
            }
            //7.存储问答记录到mongodb
            UserProblemLogPO userProblemLogPO = new UserProblemLogPO();
            userProblemLogPO.setId(id)
                    .setUserId(useAccountPO.getUserId())
                    .setAnswer(answer)
                    .setQuestion(question)
                    .setType(ProblemType.TEXT.getCode())
                    .setStatus(Status.VALID.getCode())
                    .setAskTime(now)
                    .setDialogueId(dialogId);
            if(isUpper){
                userProblemLogPO.setTokens(config.getTokens());
            }
            userProblemLogRepository.insert(userProblemLogPO);
            UseLogPO useLogPO = new UseLogPO();
            //8.不是会员就生成次数使用记录
            if(!isMember){
                useLogPO.setType(BalanceType.BRANCH_OUT)
                        .setEvent(UseAccountEvent.QUE_ANSWER)
                        .setUseAccountId(useAccountPO.getId())
                        .setCount(1L)
                        .setQuestionAnswerId(id);
                useLogService.save(useLogPO);
            }
            return userProblemLogPO;
        } catch (Exception e) {
            e.printStackTrace();
            Query query = new Query(Criteria.where("id").is(id));
            mongoTemplate.remove(query,"user_problem_log");
            throw new BusinessException(ResultCode.NETWORK_ERROR.getCode(),ResultCode.NETWORK_ERROR.getMsg());
        }
    }
}
