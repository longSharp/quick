package com.quick.member.common.enums;

public enum ImageJobType implements IBaseEnum{

    /**
     * 文生图
     */
    TEXT_GENERATE(0,"文本生图"),
    /**
     * 图生图
     */
    IMAGE_GENERATE(1,"图片生图"),
    /**
     * 选择图片
     */
    UPSCALE(2,"选择图片"),
    /**
     * 生成中
     */
    VARIATION(3,"选择生成"),
    /**
     * 重新生成
     */
    RESET(4,"重新生成"),
    /**
     * 图片生文
     */
    DESCRIBE(5,"图片生文");

    private Integer code;
    private String name;

    ImageJobType(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ImageJobType valueOf(int ordinal) {
        ImageJobType[] values = ImageJobType.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
