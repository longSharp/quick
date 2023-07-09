package com.quick.member.domain.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDialogueReqDTO {
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("对话主题id")
    private Long topicId;

    @ApiModelProperty("主题名称")
    @NotBlank
    private String name;

    @ApiModelProperty("描述介绍")
    @NotBlank
    private String describe;
}
