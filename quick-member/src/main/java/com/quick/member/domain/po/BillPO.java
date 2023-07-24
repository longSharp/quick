package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.quick.member.common.enums.FeeType;
import com.quick.member.common.enums.WriteoffStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("bill")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class BillPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("`invoice_month`")
    private LocalDate invoiceMonth;

    @TableField("`money`")
    private BigDecimal money;

    @TableField("`day_fee`")
    private BigDecimal day_fee;

    @TableField("`start_date`")
    private LocalDateTime startDate;

    @TableField("`end_date`")
    private LocalDateTime end_date;

    @TableField("`writeoff_status`")
    private WriteoffStatus writeoffStatus;

    @TableField("`total_month`")
    private Integer totalMonth;

    @TableField("`order_no`")
    private String orderNo;

    @TableField("`user_id`")
    private Long userId;

    @TableField("`product_id`")
    private Long productId;

    @TableField("`pay_record`")
    private Long payRecord;

    @TableField("`fee_type`")
    private FeeType feeType;
}
