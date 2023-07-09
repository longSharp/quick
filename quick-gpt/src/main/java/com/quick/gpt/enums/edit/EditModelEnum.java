package com.quick.gpt.enums.edit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EditModelEnum {
    /**
     * text_davinci_edit_001
     */
    TEXT_DAVINCI_EDIT_001("text-davinci-edit-001"),

    /**
     * code_davinci_edit_001
     */
    CODE_DAVINCI_EDIT_001("code-davinci-edit-001");

    /**
     * modelName
     */
    private final String modelName;
}
