package com.quick.member.common.enums;

public enum DialogTopicType implements IBaseEnum{
    /**
     * 热门推荐
     */
    POPULAR(1,"热门推荐"),
    /**
     * 实用工具
     */
    TOOLS(2,"实用工具"),
    /**
     * 文案创作
     */
    CLERK(3,"文案创作"),
    /**
     * 翻译专家
     */
    TRANSLATE(4,"翻译专家"),
    /**
     * 编程开发
     */
    PROGRAM(5,"编程开发"),
    /**
     * 知识学习
     */
    KNOWLEDGE(6,"知识学习"),
    /**
     * 生活指南
     */
    LIFE(7,"生活指南"),
    /**
     * 休闲娱乐
     */
    LEISURE(8,"休闲娱乐"),
    /**
     * 企业岗位
     */
    POST(9,"企业岗位"),
    /**
     * 其他
     */
    OTHER(10,"其他");

    private Integer code;
    private String name;

    DialogTopicType(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static DialogTopicType valueOf(int ordinal) {
        DialogTopicType[] values = DialogTopicType.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
