package com.quick.gpt.enums.embedding;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmbeddingModelEnum {
    /**
     * text-embedding-ada-002
     */
    TEXT_EMBEDDING_ADA_002("text-embedding-ada-002"),;

    /**
     * modelName
     */
    private final String modelName;
}
