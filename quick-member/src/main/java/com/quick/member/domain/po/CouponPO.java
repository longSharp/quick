package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.quick.member.common.enums.CouponOwner;
import com.quick.member.common.enums.PromotionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("coupon")
@ApiModel(value = "coupon对象", description = "优惠券表")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class CouponPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("归属(用户、产品)")
    @TableField("owner")
    private CouponOwner owner;

    @ApiModelProperty(" 优惠方式(折扣、满减、满增、赠送次数，赠送会员)")
    @TableField("promotion_type")
    private PromotionType promotionType;

    @ApiModelProperty("（折扣配置两位小数，如0.85,满减和满赠用冒号隔开，支持分段配置，多段用分号隔开,赠送次数为整数）")
    @TableField("promotion_value")
    private String promotionValue;


    @ApiModelProperty("生效开始日期")
    @TableField("start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @ApiModelProperty("生效结束开始日期")
    @TableField("end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}
