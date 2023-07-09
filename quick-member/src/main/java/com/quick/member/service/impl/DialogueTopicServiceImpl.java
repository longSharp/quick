package com.quick.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.member.common.enums.DialogTopicType;
import com.quick.member.common.enums.Status;
import com.quick.member.dao.DialogueTopicMapper;
import com.quick.member.domain.po.DialogueTopicPO;
import com.quick.member.service.DialogueTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DialogueTopicServiceImpl extends ServiceImpl<DialogueTopicMapper, DialogueTopicPO> implements DialogueTopicService {
    @Autowired
    private DialogueTopicMapper dialogueTopicMapper;

    @Override
    public List<DialogueTopicPO> getDialogueTopicsByType(Integer type) {
        LambdaQueryWrapper<DialogueTopicPO> query = new LambdaQueryWrapper<>();
        query.eq(DialogueTopicPO::getType,type).eq(DialogueTopicPO::getStatus, Status.VALID);
        return this.list(query);
    }

    @Override
    public void insert(DialogueTopicPO topic) {
        dialogueTopicMapper.insert(topic);
    }
}
