package com.quick.member.common.enums;

import com.quick.common.enums.IBaseEnum;

public enum BalanceEvent implements IBaseEnum {
    /**
     * 邀请
     */
    INVITE(0,"邀请"),
    /**
     * 提现
     */
    CASH(1,"提现"),
    /**
     * 充值
     */
    RECHARGE(2,"充值"),
    /**
     * 注册
     */
    REGISTER(3,"注册");

    private Integer code;
    private String name;

    BalanceEvent(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static BalanceEvent valueOf(int ordinal) {
        BalanceEvent[] values = BalanceEvent.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
