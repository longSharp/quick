package com.quick.member.domain.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MidjourneyReqDTO {
    @NotBlank
    private String prompt;
}
