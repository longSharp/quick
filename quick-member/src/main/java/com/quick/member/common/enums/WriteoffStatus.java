package com.quick.member.common.enums;

public enum WriteoffStatus implements IBaseEnum{
    /**
     * 已销账
     */
    CROSS_ACCOUNT(0,"已销账"),
    /**
     * 未销账
     */
    NOT_CROSS_ACCOUNT(1,"未销账"),
    /**
     * 已返销
     */
    CANCEL_CROSS_ACCOUNT(2,"已返销");

    private Integer code;
    private String name;

    WriteoffStatus(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static WriteoffStatus valueOf(int ordinal) {
        WriteoffStatus[] values = WriteoffStatus.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
