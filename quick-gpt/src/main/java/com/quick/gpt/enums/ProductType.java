package com.quick.gpt.enums;

import com.quick.common.enums.IBaseEnum;

public enum ProductType implements IBaseEnum {
    /**
     *会员日卡
     */
    MEMBER_DAY(0,"会员日卡"),
    /**
     *会员周卡
     */
    MEMBER_WEEK(1,"会员周卡"),
    /**
     *会员月卡
     */
    MEMBER_MONTH(2,"会员月卡"),
    /**
     *会员季卡
     */
    MEMBER_QUARTER(3,"会员季卡"),
    /**
     *会员年卡
     */
    MEMBER_YEAR(4,"会员年卡"),
    /**
     *会员无限卡
     */
    MEMBER_PERMANENT(5,"会员无限卡"),
    /**
     *次卡
     */
    COUNT_CARD(6,"次卡");

    private Integer code;
    private String name;

    ProductType(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ProductType valueOf(int ordinal) {
        ProductType[] values = ProductType.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
