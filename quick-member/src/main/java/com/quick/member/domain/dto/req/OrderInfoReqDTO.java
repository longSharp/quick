package com.quick.member.domain.dto.req;

import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderInfoReqDTO {
    /**
     * 订单详情
     */
    @NotBlank
    private String title;
    /**
     * 微信用户openid
     */
    private String openid;
    /**
     * 交易类型：jsapi（含小程序）、app、h5、native
     */
    @NotNull
    private TradeTypeEnum tradeType;
    /**
     * 产品id
     */
    @NotNull
    private Long productId;
    /**
     * 是否卡密购买
     */
    @NotNull
    private Boolean asCard;
}
