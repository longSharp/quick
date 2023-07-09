package com.quick.gpt.enums.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum FinishReasonEnum {
    /**
     * length
     */
    LENGTH("length"),
    STOP("stop");
    private final String message;
}
