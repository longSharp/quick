package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.quick.member.common.enums.MemberType;
import com.quick.member.common.enums.ProductType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 产品表
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Getter
@Setter
@TableName("product")
@ApiModel(value = "Product对象", description = "产品表")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class ProductPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("周卡、月卡、季卡、年卡、永久、次卡")
    @TableField("`type`")
    private ProductType type;

    @NotBlank
    @TableField("`name`")
    private String name;

    @ApiModelProperty("会员周期数（月为单位）")
    @TableField("cyc_num")
    private Integer cycNum;

    @ApiModelProperty("可使用次数")
    @TableField("use_count")
    private Long useCount;

    @ApiModelProperty("原价")
    @TableField("original_price")
    private BigDecimal originalPrice;

    @ApiModelProperty("售价")
    @TableField("sale_price")
    private BigDecimal salePrice;
}
