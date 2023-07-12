package com.quick.member.domain.dto.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ImageJobRespDTO extends AbstractRespDTO{
    private Long id;

    private Long jobFkID;

    private String img_path;

    private String jobStatus;

    private String jobSchedule;

    private String prompt;
}
