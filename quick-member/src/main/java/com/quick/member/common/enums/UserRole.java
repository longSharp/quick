package com.quick.member.common.enums;

import com.quick.common.enums.IBaseEnum;

public enum UserRole implements IBaseEnum {
    /**
     * 游客
     */
    TOURIST(0,"游客"),
    /**
     * 普通用户
     */
    USERS(1,"普通用户"),
    /**
     * 会员
     */
    MEMBER(2,"会员"),
    /**
     * 超级会员
     */
    ADMINISTRATORS_MEMBER(3,"超级会员");

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
