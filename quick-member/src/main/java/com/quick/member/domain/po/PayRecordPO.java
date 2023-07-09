package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.quick.member.common.enums.OperationType;
import com.quick.member.common.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 支付记录表
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Getter
@Setter
@TableName("`pay_record`")
@Accessors(chain = true)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class PayRecordPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("`order_no`")
    private String orderNo;

    @TableField("`user_id`")
    private Long userId;

    @TableField("operation_type")
    private OperationType operationType;

    @TableField("`money`")
    private BigDecimal money;

    @TableField("`trade_no`")
    private String tradeNo;

    @TableField("buyer_id")
    private String buyerId;

    @TableField("pay_status")
    private OrderStatus payStatus;
}
