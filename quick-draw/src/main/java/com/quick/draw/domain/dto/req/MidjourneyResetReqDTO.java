package com.quick.draw.domain.dto.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MidjourneyResetReqDTO {
    @NotNull
    private Long taskId;
}
