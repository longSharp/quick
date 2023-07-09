package com.quick.member.domain.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.quick.member.common.enums.DialogTopicType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DialogueTopicRespDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("分类")
    private Integer type;

    @ApiModelProperty("主题名称")
    private String name;

    @ApiModelProperty("描述介绍")
    private String describe;

    @ApiModelProperty("主题logo图片路径")
    private String logoPath;

    @ApiModelProperty("开场白（换行以句号分割，空行以分号分割）")
    private String prologue;

    @ApiModelProperty("提示语（多个以分号分割）")
    private String prompt;

    @ApiModelProperty("绑定的openai账号，多个用分号")
    private String openaiAccounts;

    @ApiModelProperty("顺序号")
    private Integer order;
}
