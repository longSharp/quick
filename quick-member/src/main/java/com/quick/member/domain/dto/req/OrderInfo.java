package com.quick.member.domain.dto.req;

import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import lombok.Data;

/**
 * @Author: Longcm
 * @Description: 下单信息
 */
@Data
public class OrderInfo {
    /**
     * 订单详情
     */
    private String title;
    /**
     * 微信用户openid
     */
    private String openid;
    /**
     * 交易类型：jsapi（含小程序）、app、h5、native
     */
    private TradeTypeEnum tradeType;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 是否卡密购买
     */
    private Boolean asCard;
}
