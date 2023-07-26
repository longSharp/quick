package com.quick.draw.domain.dto.resp;

import lombok.Data;

@Data
public class BaidutransResult {
    /**
     * 原文
     */
    private String src;
    /**
     * 译文
     */
    private String dst;
}
