package com.quick.member.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AliSmsParamsDTO {
    @JsonProperty("PhoneNumbers")
    private String PhoneNumbers;

    @JsonProperty("SignName")
    private String SignName;

    @JsonProperty("TemplateCode")
    private String TemplateCode;

    @JsonProperty("TemplateParam")
    private String TemplateParam;

    @JsonProperty("SmsUpExtendCode")
    private String SmsUpExtendCode;

    @JsonProperty("OutId")
    private String OutId;
}
