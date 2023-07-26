package com.quick.common.enums;

public enum Status implements IBaseEnum{
    /**
     * 失效
     */
    INVALID(0,"失效"),
    /**
     * 正常
     */
    VALID(1,"正常");

    private Integer code;
    private String name;

    Status(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Status valueOf(int ordinal) {
        Status[] values = Status.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
