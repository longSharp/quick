package com.quick.member.controller;

import com.quick.member.common.utils.TransApi;
import com.quick.member.domain.dto.req.MidjourneyReqDTO;
import com.quick.member.domain.dto.resp.ImageJobRespDTO;
import com.quick.member.domain.dto.resp.R;
import com.quick.member.domain.po.ImageJobPO;
import com.quick.member.service.ImagerJobService;
import com.quick.member.service.MidjourneyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 龙朝敏
 * @describe
 * @create 2023-07-09
 */
@Slf4j
@RestController
@RequestMapping("/image")
@Validated
public class MidjourneyController {

    @Autowired
    private MidjourneyService midjourneyService;

    @Autowired
    private ImagerJobService imagerJobService;

    @PostMapping(value = "/generate")
    public R generate(@Valid @NotNull @RequestBody MidjourneyReqDTO dto){
        midjourneyService.generateImages(dto.getPrompt(),1670413995731562497L);
        return R.ok();
    }

    @RequestMapping("/getTask")
    public R<List<ImageJobRespDTO>> getTask(@NotNull @RequestParam Integer taskStatus, HttpServletRequest request){
//        String sessionId = request.getHeader("sessionId");
//        String[] userIds = sessionId.split("-");
//        Long userId = Long.parseLong(userIds[1]);
        List<ImageJobPO> jobs = imagerJobService.getJobByUserId(1670413995731562497L,taskStatus);
        ArrayList<ImageJobRespDTO> imageJobResp = new ArrayList<>();
        for (ImageJobPO job : jobs) {
            ImageJobRespDTO imageJobRespDTO = new ImageJobRespDTO();
            String url = null;
            if(!StringUtils.isEmpty(job.getImgPath())){
                url = "http://103.144.245.137:8080/"+job.getImgPath();
            }
            imageJobRespDTO.setId(job.getId())
                    .setImg_path(url)
                    .setJobFkID(job.getJobFkID())
                    .setJobStatus(job.getJobStatus().getName())
                    .setJobSchedule(job.getJobSchedule())
                    .setPrompt(job.getPrompt())
                    .setCreateTime(job.getCreateTime())
                    .setUpdateTime(job.getUpdateTime());
            imageJobResp.add(imageJobRespDTO);
        }
        return R.ok(imageJobResp);
    }


}
