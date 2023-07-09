package com.quick.member.domain.dto.resp;

import com.quick.member.common.enums.WechatPayMentStatus;
import lombok.Data;

@Data
public class OrderStatusRespDTO {
    private WechatPayMentStatus status;
}
