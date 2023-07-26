package com.quick.member.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quick.common.dto.resp.R;
import com.quick.common.enums.Status;
import com.quick.member.common.config.params.ServiceParamsConfig;
import com.quick.member.common.enums.TopicApplication;
import com.quick.member.dao.TopicClassMapper;
import com.quick.member.domain.dto.resp.DialogueTopicRespDTO;
import com.quick.member.domain.po.DialogueTopicPO;
import com.quick.member.domain.po.TopicClassPO;
import com.quick.member.service.DialogueTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/topic")
@Validated
public class DialogueTopicController {

    @Autowired
    private DialogueTopicService dialogueTopicService;

    @Autowired
    private TopicClassMapper mapper;

    @Autowired
    private ServiceParamsConfig config;

    @RequestMapping(value = "/getDialogueTopicsByType/{typeId}")
    public R<String> getDialogueTopicsByType(@NotNull @PathVariable Long typeId){
        List<DialogueTopicPO> dialogueTopics = dialogueTopicService.getDialogueTopicsByType(typeId);
        List<DialogueTopicRespDTO> result = BeanUtil.copyToList(dialogueTopics, DialogueTopicRespDTO.class);
        List<DialogueTopicRespDTO> collect = result.stream().map(item -> {
            item.setLogoPath(config.getMinioBaseUrl() + item.getLogoPath());
            return item;
        }).collect(Collectors.toList());
        return R.ok(collect);
    }

    @RequestMapping(value = "/getDialogueTopics")
    public R<String> getDialogueTopics(){
        List<DialogueTopicPO> dialogueTopics = dialogueTopicService.list();
        List<DialogueTopicRespDTO> result = BeanUtil.copyToList(dialogueTopics, DialogueTopicRespDTO.class);
        return R.ok(result);
    }

    @PostMapping(value = "/save")
    public R save(@Valid @NotNull @RequestBody DialogueTopicPO dialogTopic){
        dialogueTopicService.insert(dialogTopic);
        return R.ok();
    }

    @RequestMapping(value = "/getTopicClassAll")
    public R<String> getTopicClassAll(){
        LambdaQueryWrapper<TopicClassPO> query = new LambdaQueryWrapper<>();
        query.eq(TopicClassPO::getStatus, Status.VALID);
        query.eq(TopicClassPO::getApplication, TopicApplication.QUES_ANS);
        List<TopicClassPO> classes = mapper.selectList(query);
        List<TopicClassPO> collect = classes.stream().map(item -> {
            item.setLogoPath(config.getMinioBaseUrl() + item.getLogoPath());
            return item;
        }).collect(Collectors.toList());
        return R.ok(collect);
    }

    @RequestMapping(value = "/loadTopic")
    public R<String> loadTopic() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<DialogueTopicPO> data = objectMapper.readValue(new File(config.getLoadTopicPath()), new TypeReference<List<DialogueTopicPO>>(){});
        dialogueTopicService.saveBatch(data);
        return R.ok(data);
    }
}
