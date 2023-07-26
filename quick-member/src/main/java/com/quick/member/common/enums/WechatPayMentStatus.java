package com.quick.member.common.enums;

import com.quick.common.enums.IBaseEnum;

public enum WechatPayMentStatus implements IBaseEnum {
    /**
     * 支付成功
     */
    SUCCESS(0,"支付成功"),
    /**
     * 转入退款
     */
    REFUND(1,"转入退款"),
    /**
     * 未支付
     */
    NOTPAY(2,"未支付"),
    /**
     * 未支付
     */
    CLOSED(3,"已关闭"),
    /**
     * 已撤销（仅付款码支付会返回）
     */
    REVOKED(4,"已撤销"),
    /**
     * 用户支付中（仅付款码支付会返回）
     */
    USERPAYING(5,"用户支付中"),
    /**
     * 支付失败（仅付款码支付会返回）
     */
    PAYERROR(6,"支付失败");


    private Integer code;
    private String name;

    WechatPayMentStatus(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static WechatPayMentStatus valueOf(int ordinal) {
        WechatPayMentStatus[] values = WechatPayMentStatus.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }

}
