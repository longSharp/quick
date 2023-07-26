package com.quick.member.common.enums;

import com.quick.common.enums.IBaseEnum;

public enum TopicApplication implements IBaseEnum {
    /**
     * 问答
     */
    QUES_ANS(0,"问答"),
    /**
     * 技能
     */
    SKILL(1,"技能"),
    /**
     * 写作
     */
    PEN_INK(2,"写作"),
    /**
     * 绘画
     */
    DRAW(3,"绘画");

    private Integer code;
    private String name;

    TopicApplication(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static TopicApplication valueOf(int ordinal) {
        TopicApplication[] values = TopicApplication.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
