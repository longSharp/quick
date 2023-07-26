package com.quick.gpt.enums;

import com.quick.common.enums.IBaseEnum;

/**
 * @author longcm
 * 余额流水类型
 */
public enum BalanceType implements IBaseEnum {

    /**
     * 支入
     */
    BRANCH_IN(0,"支入"),
    /**
     * 支出
     */
    BRANCH_OUT(1,"支出");

    private Integer code;
    private String name;

    BalanceType(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static BalanceType valueOf(int ordinal) {
        BalanceType[] values = BalanceType.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
