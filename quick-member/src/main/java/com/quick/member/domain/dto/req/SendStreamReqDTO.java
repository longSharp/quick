package com.quick.member.domain.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SendStreamReqDTO {
    @NotBlank
    private String content;
}
