package com.quick.gpt.enums;

import com.quick.common.enums.IBaseEnum;

public enum MemberMark implements IBaseEnum {

    /**
     * 非会员
     */
    UNMEMBER(0,"非会员"),
    /**
     * 会员
     */
    MEMBER(1,"会员");

    private Integer code;
    private String name;

    MemberMark(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static MemberMark valueOf(int ordinal) {
        MemberMark[] values = MemberMark.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
