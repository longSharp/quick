package com.quick.draw.common.enums;

import com.quick.common.enums.IBaseEnum;

public enum ImageProport implements IBaseEnum {

    /**
     * 1:1
     */
    ONE_ONE(0,"1:1|头像图"),
    /**
     * 1:2
     */
    ONE_TWO(1,"1:2|手机壁纸"),
    /**
     * 3:4
     */
    THREE_FOUR(2,"3:4|文案图"),
    /**
     * 4:3
     */
    FOUR_THREE(3,"4:3|社交媒体"),
    /**
     * 9:16
     */
    NINE_SIXTEEN(4,"9:16|宣传海报"),
    /**
     * 16:9
     */
    SIXTEEN_NINE(5,"16:9|海报");

    private Integer code;
    private String name;

    ImageProport(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ImageProport valueOf(int ordinal) {
        ImageProport[] values = ImageProport.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
