package com.quick.member.service;

import com.quick.member.common.enums.WechatPayMentStatus;
import com.quick.member.domain.dto.req.OrderInfo;
import com.quick.member.domain.dto.req.WechatPaymentDTO;

public interface WechatPaymentService {

    /**
     * 微信创建下单接口
     * @param orderInfo
     * @return
     */
    WechatPaymentDTO createWechatOrderAll(OrderInfo orderInfo);

    /**
     * 查询微信支付状态
     * 根据订单号
     */

    WechatPayMentStatus queryOrderForWechat(String orderNo);

    /**
     * 取消订单
     */
    void closeOrder(String orderNo);

}
