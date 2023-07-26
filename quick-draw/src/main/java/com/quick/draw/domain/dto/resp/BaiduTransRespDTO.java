package com.quick.draw.domain.dto.resp;

import lombok.Data;

import java.util.List;

@Data
public class BaiduTransRespDTO {
    /**
     * 源语言
     */
    private String from;
    /**
     * 目标语言
     */
    private String to;
    /**
     * 翻译结果
     */
    private List<BaidutransResult> trans_result;
    /**
     * 错误码:仅当出现错误时显示
     */
    private String error_code;
}
