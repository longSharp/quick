package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 卡资源表
 * </p>
 *
 * @author yangzh
 * @since 2023-07-20
 */
@Getter
@Setter
@TableName("product_coupon")
@ApiModel(value = "product_coupon对象", description = "产品优惠券表")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class ProductCouponPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("产品id")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("优惠券id")
    @TableField("coupon_id")
    private Long couponId;

}
