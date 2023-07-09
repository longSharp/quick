package com.quick.member.common.enums;

public enum UserStatus implements IBaseEnum{
    /**
     * 正常
     */
    NORMAL(0,"正常"),
    /**
     * 黑名单
     */
    BLACK_LIST(1,"黑名单"),
    /**
     * 临时冻结
     */
    TEM_FREEZE(2,"临时冻结"),
    /**
     * 永久冻结
     */
    PER_FREEZE(3,"永久冻结"),
    ;

    private Integer code;
    private String name;

    UserStatus(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static UserStatus valueOf(int ordinal) {
        UserStatus[] values = UserStatus.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
