package com.quick.member.common.enums;

import com.quick.common.enums.IBaseEnum;

public enum FeeType implements IBaseEnum {
    /**
     * 周期性
     */
    CYCLE(0,"周期性"),
    /**
     * 一次性
     */
    COUNT(1,"一次性");

    private Integer code;
    private String name;

    FeeType(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static FeeType valueOf(int ordinal) {
        FeeType[] values = FeeType.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
