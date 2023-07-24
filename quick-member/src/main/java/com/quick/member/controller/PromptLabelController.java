package com.quick.member.controller;

import cn.hutool.core.bean.BeanUtil;
import com.quick.member.domain.dto.resp.PromptProptRespDTO;
import com.quick.member.domain.dto.resp.R;
import com.quick.member.domain.po.PromptLabelPO;
import com.quick.member.service.PromptLabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tags")
@Validated
public class PromptLabelController {

    @Autowired
    private PromptLabelService promptLabelService;

    @RequestMapping("/getProptPrompt")
    public R<String> getProptPrompt(){
        List<PromptLabelPO> proptPrompt = promptLabelService.getProptPrompt();
        List<PromptProptRespDTO> data = new ArrayList<>();
        for (PromptLabelPO promptLabelPO : proptPrompt) {
            PromptProptRespDTO promptProptRespDTO = new PromptProptRespDTO();
            promptProptRespDTO.setId(promptLabelPO.getId());
            String name = promptLabelPO.getName();
            String[] split = name.split("\\|");
            if(split.length>1){
                promptProptRespDTO.setName(split[0]);
                promptProptRespDTO.setDesc(split[1]);
            }else{
                promptProptRespDTO.setName(name);
            }
            data.add(promptProptRespDTO);
        }
        return R.ok(data);
    }
}
