package com.quick.member.domain.dto.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class MidjourneyGenerateReqDTO {
    /**
     * 提示词
     */
    @Length(min = 2,max = 2000)
    @NotBlank
    private String prompt;
    /**
     * 图片比例
     */
    private List<Long> tags;
}
