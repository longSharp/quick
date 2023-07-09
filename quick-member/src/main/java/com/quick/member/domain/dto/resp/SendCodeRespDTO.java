package com.quick.member.domain.dto.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendCodeRespDTO {
    private Boolean registered;
    private String code;
}
