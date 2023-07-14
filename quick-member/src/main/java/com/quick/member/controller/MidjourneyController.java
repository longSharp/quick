package com.quick.member.controller;

import cn.hutool.json.JSONUtil;
import com.quick.member.common.constant.RedisKeyPrefixConstant;
import com.quick.member.common.enums.ImageJobType;
import com.quick.member.common.enums.ResultCode;
import com.quick.member.common.utils.UserHolder;
import com.quick.member.domain.dto.req.MidjourneyGenerateReqDTO;
import com.quick.member.domain.dto.req.MidjourneyUpsampleReqDTO;
import com.quick.member.domain.dto.resp.ImageJobRespDTO;
import com.quick.member.domain.dto.resp.R;
import com.quick.member.domain.po.ImageJobPO;
import com.quick.member.service.ImagerJobService;
import com.quick.member.service.MidjourneyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
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
    public R generate(@Valid @NotNull @RequestBody MidjourneyGenerateReqDTO dto){
        Long userId = UserHolder.getUserId();
        midjourneyService.generateImages(dto,userId);
        return R.ok();
    }

    @PostMapping(value = "/imageGenerate")
    public R imageGenerateImage(@Valid @NotNull @RequestBody MultipartFile file,@Valid @NotNull @RequestBody MidjourneyGenerateReqDTO dto){
        Long userId = UserHolder.getUserId();
        return R.ok();
//        try {
//            midjourneyService.imageGenerateImage(file,dto,userId);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return R.error(ResultCode.GENERATE_FAIL);
//        }
//        return R.ok();
    }

    @PostMapping(value = "/upsample")
    public R upsample(@Valid @NotNull @RequestBody MidjourneyUpsampleReqDTO dto){
        midjourneyService.upsample(dto);
        return R.ok();
    }

    @RequestMapping("/getTask")
    public R<List<ImageJobRespDTO>> getTask(@NotNull @RequestParam Integer taskStatus){
        Long userId = UserHolder.getUserId();
        List<ImageJobPO> jobs = imagerJobService.getJobByUserId(userId,taskStatus);
        ArrayList<ImageJobRespDTO> imageJobResp = new ArrayList<>();
        for (ImageJobPO job : jobs) {
            ImageJobRespDTO imageJobRespDTO = new ImageJobRespDTO();
            String url = null;
            if(!StringUtils.isEmpty(job.getImgPath())){
                url = "http://103.144.245.137:8080/"+job.getImgPath();
            }
            ImageJobType jobType = job.getJobType();
            imageJobRespDTO.setId(job.getId())
                    .setImg_path(url)
                    .setJobFkID(job.getJobFkID())
                    .setJobStatus(job.getJobStatus().getName())
                    .setJobSchedule(job.getJobSchedule())
                    .setPrompt(job.getPrompt())
                    .setCreateTime(job.getCreateTime())
                    .setUpdateTime(job.getUpdateTime());
            if(jobType!=null){
                Integer code = jobType.getCode();
                String name = jobType.getName();
                imageJobRespDTO.setTypeId(code);
                imageJobRespDTO.setTypeName(name);
            }
            imageJobResp.add(imageJobRespDTO);
        }
        return R.ok(imageJobResp);
    }


}
