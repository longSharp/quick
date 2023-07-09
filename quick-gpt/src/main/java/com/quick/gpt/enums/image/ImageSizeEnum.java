package com.quick.gpt.enums.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageSizeEnum {

    /**
     * 256x256
     */
    S256x256("256x256"),
    S512x512("512x512"),
    S1024x1024("1024x1024");
    private final String size;
}
