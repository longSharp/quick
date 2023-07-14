package com.quick.member.service;

import com.quick.member.domain.dto.req.MidjourneyGenerateReqDTO;
import com.quick.member.domain.dto.req.MidjourneyUpsampleReqDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MidjourneyService {
    /**
     * 生成图片
     * @param prompt
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
     * 上传文件
     */
    String upload(byte[] bytes,String fileName,long fileSize);
}
