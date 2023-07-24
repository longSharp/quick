package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.quick.member.common.enums.MemberType;
import com.quick.member.common.enums.ProductType;
import com.quick.member.common.enums.SaleStatus;
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

    @ApiModelProperty("有效周期（周、月、季、年）")
    @TableField("`validity_period`")
    private ProductType type;

    @NotBlank
    @TableField("`name`")
    private String name;

    @ApiModelProperty(" 绘画权益次数（0：不允许；999999国际化成不限制）")
    @TableField("draw_count")
    private Long drawCount;

    @ApiModelProperty("对话权益次数")
    @TableField("use_count")
    private Long useCount;

    @ApiModelProperty("原价")
    @TableField("original_price")
    private BigDecimal originalPrice;

    @ApiModelProperty("售价")
    @TableField("sale_price")
    private BigDecimal salePrice;

    @ApiModelProperty("（会员、超级会员、固定充值卡、任意充值卡）")
    @TableField("`product_type`")
    private ProductType productType;

    @ApiModelProperty(" (上架、下架)")
    @TableField("`sale_status`")
    private SaleStatus saleStatus;

    @ApiModelProperty("备注")
    @TableField("`REMARK`")
    private String remark;
}
