package com.quick.member.common.enums;

import com.quick.common.enums.IBaseEnum;

/**
 * @Auther: yangzh
 * @Date: 2023/7/20 15:45
 * @Description:
 */
public enum SaleStatus implements IBaseEnum {
    /**
     * 上架
     */
    ON(0,"上架"),
    /**
     * 下架
     */
    OFF(1,"下架");

    private Integer code;
    private String name;

    SaleStatus(Integer code,String name){
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

    public static SaleStatus valueOf(int ordinal) {
        SaleStatus[] values = SaleStatus.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
