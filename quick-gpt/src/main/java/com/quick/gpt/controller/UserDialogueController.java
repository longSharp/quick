package com.quick.gpt.controller;

import cn.hutool.core.bean.BeanUtil;
import com.quick.common.config.UserHolder;
import com.quick.common.dto.resp.R;
import com.quick.gpt.domain.dto.req.UserDialogueReqDTO;
import com.quick.gpt.domain.dto.resp.UserDialogueRespDTO;
import com.quick.gpt.domain.po.UserDialoguePO;
import com.quick.gpt.service.UserDialogueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dialogue")
@Validated
public class UserDialogueController {

    @Autowired
    private UserDialogueService userDialogueService;

    @RequestMapping(value = "/getDialogueByUserId/{topic}")
    public R<String> getDialogueByUserId(@NotNull @PathVariable Integer topic){
        Boolean isTopic = topic==1;
        List<UserDialoguePO> dialogues = userDialogueService.getDialogueByUserId(UserHolder.getUserId(),isTopic);
        return R.ok(BeanUtil.copyToList(dialogues, UserDialogueRespDTO.class));
    }

    @RequestMapping(value = "/getDialogueByName")
    public R<String> getDialogueByName(@NotNull @RequestParam Integer topic,@RequestParam String name){
        Boolean isTopic = topic==1;
        List<UserDialoguePO> dialogues = userDialogueService.getDialogueByName(UserHolder.getUserId(),name,isTopic);
        return R.ok(BeanUtil.copyToList(dialogues,UserDialogueRespDTO.class));
    }

    @RequestMapping(value = "/modifyDialogue", method = RequestMethod.POST)
    public R<String> modifyDialogue(@Valid @NotNull @RequestBody UserDialogueReqDTO userDisalogs){
        UserDialoguePO userDialoguePO = BeanUtil.copyProperties(userDisalogs, UserDialoguePO.class);
        userDialoguePO.setUserId(UserHolder.getUserId());
        userDialogueService.modifyDialogue(userDialoguePO);
        return R.ok(BeanUtil.copyProperties(userDialoguePO,UserDialogueRespDTO.class));
    }

    @RequestMapping(value = "/deleteDialogueById/{id}", method = RequestMethod.POST)
    public R deleteDialogueById(@Valid @NotNull @PathVariable Long id){
        userDialogueService.deleteDialogueById(id);
        return R.ok();
    }

    @RequestMapping(value = "/addDialogue", method = RequestMethod.POST)
    public R<String> addDialogue(@Valid @NotNull @RequestBody UserDialogueReqDTO userDisalogs){
        UserDialoguePO userDialoguePO = BeanUtil.copyProperties(userDisalogs, UserDialoguePO.class);
        userDialoguePO.setUserId(UserHolder.getUserId());
        userDialogueService.addDialogue(userDialoguePO);
        return R.ok(BeanUtil.copyProperties(userDialoguePO,UserDialogueRespDTO.class));
    }
}
