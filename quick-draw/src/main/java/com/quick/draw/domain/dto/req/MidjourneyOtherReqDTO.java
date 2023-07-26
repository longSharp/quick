package com.quick.draw.domain.dto.req;

import com.quick.draw.common.enums.ImageJobType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MidjourneyOtherReqDTO {
    @NotNull
    private Long taskId;

    @NotNull
    private ImageJobType type;
}
