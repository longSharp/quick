package com.quick.member.common.enums;

import com.quick.common.enums.IBaseEnum;

public enum MemberType implements IBaseEnum {
    /**
     * 日卡
     */
    DAY_CARD(0,"日卡"),
    /**
     * 日卡
     */
    WEEK_CARD(1,"日卡"),
    /**
     * 月卡
     */
    MONTH_CARD(2,"月卡"),
    /**
     * 季卡
     */
    QUART_CARD(3,"季卡"),
    /**
     * 年卡
     */
    YEAR_CARD(4,"年卡"),
    /**
     * 永久
     */
    PERMANENT_CARD(5,"永久");

    private Integer code;
    private String name;

    MemberType(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static MemberType valueOf(int ordinal) {
        MemberType[] values = MemberType.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
