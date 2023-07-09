package com.quick.member.domain.dto.req;

import lombok.Data;

@Data
public class WechatNotifyResourceReqDTO {
    private String mchid;
    private String appid;
    private String out_trade_no;
    private String transaction_id;
    private String trade_type;
    private String trade_state;
    private String trade_state_desc;
    private String bank_type;
    private String attach;
    private String success_time;
    private WechatPayerReqDTO payer;
}
