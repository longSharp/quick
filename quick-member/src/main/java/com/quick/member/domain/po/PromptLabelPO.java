package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@TableName("prompt_label")
@ApiModel(value = "prompt_label对象", description = "提示词标签表")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class PromptLabelPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("`prompt_label_id`")
    private Long promptLabelId;

    @TableField("`name`")
    private String name;

    @TableField("`template`")
    private String template;

    @TableField("`currency_num`")
    private Integer currencyNum;

    @TableField("`param`")
    private String param;

    @TableField("`level`")
    private Integer level;

    @TableField("`topic_class_id`")
    private Long topicClassId;
}
