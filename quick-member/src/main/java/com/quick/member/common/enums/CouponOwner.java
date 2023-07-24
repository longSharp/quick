package com.quick.member.common.enums;

/**
 * @Auther: yangzh
 * @Date: 2023/7/20 15:38
 * @Description:
 */
public enum CouponOwner implements IBaseEnum{
    /**
     * 用户
     */
    SUBSCRIBER(0,"用户"),
    /**
     * 产品
     */
    PRODUCT(1,"产品");

    private Integer code;
    private String name;

    CouponOwner(Integer code,String name){
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

    public static CouponOwner valueOf(int ordinal) {
        CouponOwner[] values = CouponOwner.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
