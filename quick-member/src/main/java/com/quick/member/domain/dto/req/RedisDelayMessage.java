package com.quick.member.domain.dto.req;

import lombok.Data;

@Data
public class RedisDelayMessage {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户id
     */
    private Long userId;
}
