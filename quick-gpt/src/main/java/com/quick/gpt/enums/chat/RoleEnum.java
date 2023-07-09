package com.quick.gpt.enums.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    /**
     * system
     */
    SYSTEM("system"),

    /**
     * assistant
     */
    ASSISTANT("assistant"),

    /**
     * user
     */
    USER("user");

    private final String roleName;
}
