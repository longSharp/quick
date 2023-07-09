package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.quick.member.common.enums.BalanceEvent;
import com.quick.member.common.enums.BalanceType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 余额流水记录表
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("balance_log")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class BalanceLogPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("`type`")
    private BalanceType type;

    @TableField("`event`")
    private BalanceEvent event;

    @TableField("`use_invit_code`")
    private String useInvitCode;

    @TableField("`account_id`")
    private Long accountId;

    @TableField("`order_no`")
    private String orderNo;

    @ApiModelProperty("流水金额")
    @TableField("money")
    private BigDecimal money;
}
