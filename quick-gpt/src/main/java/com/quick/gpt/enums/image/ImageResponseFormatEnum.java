package com.quick.gpt.enums.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageResponseFormatEnum {
    /**
     * url
     */
    URL("url"),
    B64_JSON("b64_json");
    private final String responseFormat;
}
