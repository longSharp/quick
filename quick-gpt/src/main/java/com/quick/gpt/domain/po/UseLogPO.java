package com.quick.gpt.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.quick.gpt.enums.BalanceType;
import com.quick.gpt.enums.UseAccountEvent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 次数使用记录表
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("use_log")
@ApiModel(value = "UseLog对象", description = "次数使用记录表")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class UseLogPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("类型（支入、支出）")
    @TableField("`type`")
    private BalanceType type;

    @ApiModelProperty("事件（注册、购买产品、问答、邀请）")
    @TableField("`event`")
    private UseAccountEvent event;

    @ApiModelProperty("订单号")
    @TableField("order_no")
    private String orderNo;

    @ApiModelProperty("问答id")
    @TableField("question_answer_id")
    private String questionAnswerId;

    @ApiModelProperty("用户账户id")
    @TableField("use_account_id")
    private Long useAccountId;

    @TableField("`use_invit_code`")
    private String useInvitCode;

    @ApiModelProperty("使用次数")
    @TableField("count")
    private Long count;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;
}
