package com.quick.member.common.enums;

public enum UserRole implements IBaseEnum{
    /**
     * 游客
     */
    TOURIST(0,"游客"),
    /**
     * 普通用户
     */
    USERS(1,"普通用户"),
    /**
     * 超级管理员
     */
    ADMINISTRATORS(2,"超级管理员");

    private Integer code;
    private String name;

    UserRole(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static UserRole valueOf(int ordinal) {
        UserRole[] values = UserRole.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
