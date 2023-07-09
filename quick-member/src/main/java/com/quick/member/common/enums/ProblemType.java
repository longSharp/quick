package com.quick.member.common.enums;

public enum ProblemType implements IBaseEnum{
    /**
     * 已支付
     */
    TEXT(0,"文字"),
    /**
     * 未支付
     */
    IMAGE(1,"图片");

    private Integer code;
    private String name;

    ProblemType(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ProblemType valueOf(int ordinal) {
        ProblemType[] values = ProblemType.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
