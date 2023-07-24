package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.quick.member.common.enums.DialogTopicType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@TableName("dialogue_topic")
@ApiModel(value = "dialogue_topic", description = "会话主题表")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class DialogueTopicPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("分类")
    @TableField("`topic_class_id`")
    private Long topicClassId;

    @ApiModelProperty("主题名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("描述介绍")
    @TableField("`describe`")
    private String describe;

    @ApiModelProperty("主题logo图片路径")
    @TableField("logo_path")
    private String logoPath;

    @ApiModelProperty("训练的模板语言")
    @TableField("template")
    private String template;

    @ApiModelProperty("开场白（换行以句号分割，空行以分号分割）")
    @TableField("prologue")
    private String prologue;

    @ApiModelProperty("提示语（多个以分号分割）")
    @TableField("prompt")
    private String prompt;

    @ApiModelProperty("绑定的openai账号，多个用分号")
    @TableField("openai_accounts")
    private String openaiAccounts;

    @ApiModelProperty("顺序号")
    @TableField("`order`")
    private Integer order;

    @ApiModelProperty("消耗货币数(默认值 1)")
    @TableField("`currency_num`")
    private Integer currencyNum;

}
