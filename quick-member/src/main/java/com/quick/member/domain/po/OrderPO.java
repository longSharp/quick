package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.quick.member.common.enums.OrderStatus;
import com.quick.member.common.enums.ProductType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单记录表
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Getter
@Setter
@TableName("`order`")
@Accessors(chain = true)
@ApiModel(value = "Order对象", description = "订单记录表")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class OrderPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户id")
    @TableField("`user_id`")
    private Long userId;

    @ApiModelProperty("产品类型名称")
    @TableField("`product_type`")
    private ProductType productType;

    @ApiModelProperty("可使用次数")
    @TableField("`use_count`")
    private Long useCount;

    @ApiModelProperty("原价")
    @TableField("`original_price`")
    private BigDecimal originalPrice;

    @ApiModelProperty("产品id")
    @TableField("`product_id`")
    private Long productId;

    @ApiModelProperty("订单金额")
    @TableField("`money`")
    private BigDecimal money;

    @ApiModelProperty("支付状态（已支付/未支付）")
    @TableField("`order_status`")
    private OrderStatus orderStatus;

    @ApiModelProperty("订单号")
    @TableField("`order_no`")
    private String orderNo;
}
