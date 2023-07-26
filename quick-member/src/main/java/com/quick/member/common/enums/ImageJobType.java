package com.quick.member.common.enums;

import com.quick.common.enums.IBaseEnum;

public enum ImageJobType implements IBaseEnum {

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
     * 选择生成
     */
    VARIATION(3,"选择生成"),
    /**
     * 重新生成
     */
    RESET(4,"重新生成"),
    /**
     * 图片生文
     */
    DESCRIBE(5,"图片生文"),
    /**
     * 重调图片
     */
    HIGH_VARIATION(6,"重调图片"),
    /**
     * 微调图片
     */
    LOW_VARIATION(7,"微调图片"),
    /**
     * 向左扩展
     */
    PAN_LEFT(8,"向左扩展"),
    /**
     * 向右扩展
     */
    PAN_RIGHT(9,"向右扩展"),
    /**
     * 向上扩展
     */
    PAN_UP(10,"向上扩展"),
    /**
     * 向下扩展
     */
    PAN_DOWN(11,"向下扩展"),
    /**
     * 放大2倍
     */
    OUTPAINT_2(12,"放大2倍"),
    /**
     * 放大1.5倍
     */
    OUTPAINT_1_5(13,"放大1.5倍");

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
