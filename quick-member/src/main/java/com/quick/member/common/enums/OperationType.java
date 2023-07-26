package com.quick.member.common.enums;

import com.quick.common.enums.IBaseEnum;

public enum OperationType implements IBaseEnum {
    /**
     * 会员充值
     */
    MEMBER_RECHARGE(0,"会员充值"),
    /**
     * 次数充值
     */
    COUNT_RECHARGE(1,"次数充值"),
    /**
     * 余额充值
     */
    BALANCE_RECHARGE(2,"余额充值"),
    /**
     * 卡密购买
     */
    CARD_RECHARGE(3,"卡密购买");

    private Integer code;
    private String name;

    OperationType(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static OperationType valueOf(int ordinal) {
        OperationType[] values = OperationType.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
