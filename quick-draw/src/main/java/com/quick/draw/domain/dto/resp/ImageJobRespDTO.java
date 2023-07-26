package com.quick.draw.domain.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImageJobRespDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long jobFkID;

    private String img_path;

    private String jobStatus;

    private String jobSchedule;

    private String prompt;

    private Integer typeId;

    private String typeName;

    private String rule;

    private String createTime;

    private String updateTime;
}
