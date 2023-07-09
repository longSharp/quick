package com.quick.member.domain.dto.req;

import lombok.Data;

@Data
public class WechatPaymentDTO {
    /**
     * native支付返回
     */
    private String codeUrl;
    /**
     * jsapi，app支付返回
     */
    private String prepayId;
    /**
     * h5支付返回
     */
    private String h5Url;
    /**
     * 订单号
     */
    private String orderNo;
}
