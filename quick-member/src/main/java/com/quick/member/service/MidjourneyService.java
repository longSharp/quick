package com.quick.member.service;

public interface MidjourneyService {
    /**
     * 生成图片
     * @param prompt
     */
    void generateImages(String prompt,Long userId);
}
