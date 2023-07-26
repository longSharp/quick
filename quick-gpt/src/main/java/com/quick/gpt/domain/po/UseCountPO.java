package com.quick.gpt.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@TableName("use_count")
@Accessors(chain = true)
@ApiModel(value = "用户使用量", description = "用户使用量")
public class UseCountPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("token数")
    @TableField("token_count")
    private Long tokenCount;

    @ApiModelProperty("对话次数")
    @TableField("dialogue_count")
    private Long dialogueCount;

    @ApiModelProperty("绘画次数")
    @TableField("draw_count")
    private Long drawCount;

}
