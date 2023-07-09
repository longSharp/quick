package com.quick.member.domain.dto.req;

import lombok.Data;

@Data
public class ChatGptModelDTO {
    /**
     *
     * 角色
     * */
    private String role;

    /**
     *
     * 对话的内容
     * */
    private String content;
}
