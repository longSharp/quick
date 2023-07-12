package com.quick.member.common.enums;

public enum ImageJobStatus implements IBaseEnum{

    /**
     * 创建中
     */
    CREATING(0,"创建中"),
    /**
     * 已创建
     */
    CREATED(1,"已创建"),
    /**
     * 生成中
     */
    GENERATING(2,"生成中"),
    /**
     * 生成中
     */
    GENERATED(3,"已生成"),
    /**
     * 生成中
     */
    FAIL(4,"生成失败");

    private Integer code;
    private String name;

    ImageJobStatus(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ImageJobStatus valueOf(int ordinal) {
        ImageJobStatus[] values = ImageJobStatus.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
