package com.quick.member.controller;

import cn.hutool.core.bean.BeanUtil;
import com.quick.member.domain.dto.req.UserDialogueReqDTO;
import com.quick.member.domain.dto.resp.R;
import com.quick.member.domain.dto.resp.UserDialogueRespDTO;
import com.quick.member.domain.po.UserDialoguePO;
import com.quick.member.service.UserDialogueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    public R<List<UserDialogueRespDTO>> getDialogueByUserId(@NotNull @PathVariable Integer topic, HttpServletRequest request){
        String sessionId = request.getHeader("sessionId");
        String[] userIds = sessionId.split("-");
        Boolean isTopic = topic==1;
        List<UserDialoguePO> dialogues = userDialogueService.getDialogueByUserId(Long.parseLong(userIds[1]),isTopic);
        return R.ok(BeanUtil.copyToList(dialogues,UserDialogueRespDTO.class));
    }

    @RequestMapping(value = "/getDialogueByName")
    public R<List<UserDialogueRespDTO>> getDialogueByName(@NotNull @RequestParam Integer topic,@RequestParam String name, HttpServletRequest request){
        String sessionId = request.getHeader("sessionId");
        String[] userIds = sessionId.split("-");
        Boolean isTopic = topic==1;
        List<UserDialoguePO> dialogues = userDialogueService.getDialogueByName(Long.parseLong(userIds[1]),name,isTopic);
        return R.ok(BeanUtil.copyToList(dialogues,UserDialogueRespDTO.class));
    }

    @RequestMapping(value = "/modifyDialogue", method = RequestMethod.POST)
    public R<UserDialogueRespDTO> modifyDialogue(@Valid @NotNull @RequestBody UserDialogueReqDTO userDisalogs,HttpServletRequest request){
        String sessionId = request.getHeader("sessionId");
        String[] userIds = sessionId.split("-");
        UserDialoguePO userDialoguePO = BeanUtil.copyProperties(userDisalogs, UserDialoguePO.class);
        userDialoguePO.setUserId(Long.parseLong(userIds[1]));
        userDialogueService.modifyDialogue(userDialoguePO);
        return R.ok(BeanUtil.copyProperties(userDialoguePO,UserDialogueRespDTO.class));
    }

    @RequestMapping(value = "/deleteDialogueById/{id}", method = RequestMethod.POST)
    public R deleteDialogueById(@Valid @NotNull @PathVariable Long id){
        userDialogueService.deleteDialogueById(id);
        return R.ok();
    }

    @RequestMapping(value = "/addDialogue", method = RequestMethod.POST)
    public R<UserDialogueRespDTO> addDialogue(@Valid @NotNull @RequestBody UserDialogueReqDTO userDisalogs,HttpServletRequest request){
        String sessionId = request.getHeader("sessionId");
        String[] userIds = sessionId.split("-");
        UserDialoguePO userDialoguePO = BeanUtil.copyProperties(userDisalogs, UserDialoguePO.class);
        userDialoguePO.setUserId(Long.parseLong(userIds[1]));
        userDialogueService.addDialogue(userDialoguePO);
        return R.ok(BeanUtil.copyProperties(userDialoguePO,UserDialogueRespDTO.class));
    }
}
