package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.quick.member.common.enums.MemberType;
import com.quick.member.common.enums.ProductType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Getter
@Setter
@TableName("user_member")
@Accessors(chain = true)
@ApiModel(value = "UserMember对象", description = "会员表")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class UserMemberPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户id")
    @TableField("`user_id`")
    private Long userId;

    @ApiModelProperty("会员类型（月卡、季度、年卡）")
    @TableField("`type`")
    private ProductType type;

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

    @ApiModelProperty("订单号")
    @TableField("order_no")
    private String orderNo;

    @ApiModelProperty("剩余次数")
    @TableField("dialog_balance")
    private Long dialogBalance;

    @ApiModelProperty("剩余次数更新时间")
    @TableField("last_dialog_time")
    private LocalDateTime lastDialogTime;

    @ApiModelProperty("绘画剩余次数")
    @TableField("draw_balance")
    private Long drawBalance;

    @ApiModelProperty("最后一次绘画时间")
    @TableField("last_draw_time")
    private LocalDateTime lastDrawTime;

}
