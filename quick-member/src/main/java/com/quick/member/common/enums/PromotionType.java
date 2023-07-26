package com.quick.member.common.enums;

import com.quick.common.enums.IBaseEnum;

/**
 * @Auther: yangzh
 * @Date: 2023/7/20 15:40
 * @Description:
 */
public enum PromotionType implements IBaseEnum {
    /**
     * 折扣
     */
    DISCOUNT(0,"折扣"),
    /**
     * 满减
     */
    FULL_REDUCTION(1,"满减"),
    /**
     * 满增
     */
    FULL_INCREASE(2,"满增"),
    /**
     * 赠送次数
     */
    FREE_TIMES(3,"赠送次数"),

    /**
     * 赠送会员
     */
    FREE_MEMBERSHIP(4,"赠送会员");

    private Integer code;
    private String name;

    PromotionType(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    public static PromotionType valueOf(int ordinal) {
        PromotionType[] values = PromotionType.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
