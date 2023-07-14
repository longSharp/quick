package com.quick.member.domain.dto.req;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class MidjourneyUpsampleReqDTO {
    @NotNull
    private Long taskId;
    @Min(1)
    @Max(4)
    @NotNull
    private Integer index;
}
