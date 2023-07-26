package com.quick.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.common.enums.Status;
import com.quick.member.dao.PromptLabelMapper;
import com.quick.member.domain.po.PromptLabelPO;
import com.quick.member.service.PromptLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromptLabelServiceImpl implements PromptLabelService {

    @Autowired
    private PromptLabelMapper promptLabelMapper;

    @Override
    public List<PromptLabelPO> getProptPrompt() {
        LambdaQueryWrapper<PromptLabelPO> query = new LambdaQueryWrapper<>();
        query.eq(PromptLabelPO::getName,"图片比例").eq(PromptLabelPO::getLevel,1);
        List<PromptLabelPO> father = promptLabelMapper.selectList(query);
        if(father.size()==0){
            return new ArrayList<>();
        }
        LambdaQueryWrapper<PromptLabelPO> sonQuery = new LambdaQueryWrapper<>();
        sonQuery.eq(PromptLabelPO::getPromptLabelId,father.get(0).getId()).eq(PromptLabelPO::getStatus, Status.VALID);
        return promptLabelMapper.selectList(sonQuery);
    }
}
