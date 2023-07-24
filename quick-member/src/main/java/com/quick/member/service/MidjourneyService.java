package com.quick.member.service;

import com.quick.member.domain.dto.req.MidjourneyGenerateReqDTO;
import com.quick.member.domain.dto.req.MidjourneyOtherReqDTO;
import com.quick.member.domain.dto.req.MidjourneyResetReqDTO;
import com.quick.member.domain.dto.req.MidjourneyUpsampleReqDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MidjourneyService {
    /**
     * 生成图片
     */
    void generateImages(MidjourneyGenerateReqDTO dto, Long userId);

    /**
     * 选择图片
     */
    void upsample(MidjourneyUpsampleReqDTO dto);

    /**
     * 图生图
     */
    void imageGenerateImage(MultipartFile file, MidjourneyGenerateReqDTO dto,Long userId) throws IOException;

    /**
     * 重新生成
     */
    void reset(MidjourneyResetReqDTO dto);

    /**
     * 选择图片重新生成
     */
    void variation(MidjourneyUpsampleReqDTO dto);

    /**
     * 其他扩展生成
     */
    void other(MidjourneyOtherReqDTO dto, Integer index);


    /**
     * 上传文件
     */
    String upload(MultipartFile file) throws IOException;
}
