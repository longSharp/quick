package com.quick.member.common.enums;

public enum CardStatus implements IBaseEnum {
    /**
     * 未使用
     */
    UNUSE(0,"未使用"),
    /**
     * 占用
     */
    OCCUPY(1,"占用"),
    /**
     * 已使用
     */
    USED(2,"已使用");

    private Integer code;
    private String name;

    CardStatus(Integer code,String name){
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

    public static CardStatus valueOf(int ordinal) {
        CardStatus[] values = CardStatus.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
