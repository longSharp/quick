package com.quick.member.common.enums;

public enum UseAccountEvent implements IBaseEnum{
    /**
     * 注册
     */
    REGISTER(0,"注册"),
    /**
     * 次卡购买
     */
    CARD_PURCHASE(1,"次卡购买"),
    /**
     * 卡密兑换
     */
    CARMI(2,"卡密兑换"),
    /**
     * 问答
     */
    QUE_ANSWER(3,"问答"),
    /**
     * 邀请
     */
    INVITE(4,"邀请"),
    /**
     * 签到
     */
    ATTENDANCE(5,"签到");

    private Integer code;
    private String name;

    UseAccountEvent(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static UseAccountEvent valueOf(int ordinal) {
        UseAccountEvent[] values = UseAccountEvent.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
