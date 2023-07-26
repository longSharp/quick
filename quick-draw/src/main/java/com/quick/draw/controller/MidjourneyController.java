package com.quick.draw.controller;

import com.quick.common.dto.resp.R;
import com.quick.draw.common.enums.ImageJobType;
import com.quick.draw.domain.dto.req.*;
import com.quick.draw.domain.dto.resp.ImageJobRespDTO;
import com.quick.draw.domain.po.AbstractPO;
import com.quick.draw.domain.po.ImageJobPO;
import com.quick.draw.service.ImagerJobService;
import com.quick.draw.service.MidjourneyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
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
        dto.setPrompt(dto.getPrompt().replace("--","").replace("**","").replace(">","").replace("<","").trim());
        midjourneyService.generateImages(dto,userId);
        return R.ok();
    }

    @PostMapping(value = "/imageGenerate")
    public R imageGenerateImage(@Valid @NotNull MultipartFile file,@Valid @NotNull MidjourneyGenerateReqDTO dto) {
        Long userId = UserHolder.getUserId();
        dto.setPrompt(dto.getPrompt().replace("--","").replace("**","").replace(">","").replace("<","").trim());
//        try {
//            midjourneyService.imageGenerateImage(file,dto,userId);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return R.error(ResultCode.GENERATE_FAIL);
//        }
        return R.ok();
    }

    @PostMapping(value = "/reset")
    public R reset(@Valid @NotNull @RequestBody MidjourneyResetReqDTO dto){
        midjourneyService.reset(dto);
        return R.ok();
    }

    @PostMapping(value = "/other")
    public R other(@Valid @NotNull @RequestBody MidjourneyOtherReqDTO dto){
        midjourneyService.other(dto,null);
        return R.ok();
    }

    @PostMapping(value = "/upsample")
    public R upsample(@Valid @NotNull @RequestBody MidjourneyUpsampleReqDTO dto){
        midjourneyService.upsample(dto);
        return R.ok();
    }

    @PostMapping(value = "/variation")
    public R variation(@Valid @NotNull @RequestBody MidjourneyUpsampleReqDTO dto){
        midjourneyService.variation(dto);
        return R.ok();
    }

    @RequestMapping("/getTask")
    public R<String> getTask(@NotNull @RequestParam Integer taskStatus){
        Long userId = UserHolder.getUserId();
        List<ImageJobPO> jobs = imagerJobService.getJobByUserId(userId,taskStatus);
        List<ImageJobRespDTO> imageJobResp = new ArrayList<>();
        // 创建一个比较器，按时间升序排序
        Comparator<ImageJobPO> comparator = Comparator.comparing(AbstractPO::getCreateTime);
        // 使用比较器对list进行排序
        jobs.sort(comparator);
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
                    .setRule(job.getRule())
                    .setCreateTime(job.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .setUpdateTime(job.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
