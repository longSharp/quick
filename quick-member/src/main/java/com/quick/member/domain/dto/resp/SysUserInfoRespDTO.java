package com.quick.member.domain.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.quick.member.common.enums.MemberMark;
import com.quick.member.common.enums.UserRole;
import com.quick.member.common.enums.UserStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SysUserInfoRespDTO extends AbstractRespDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("手机号")
    private String tel;

    @ApiModelProperty("用户角色")
    private UserRole role;

    @ApiModelProperty("是否是会员")
    private MemberMark isMember;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty("邀请人邀请码")
    private String invitePersonCode;

    @ApiModelProperty("头像")
    private String profilePhoto;

    @ApiModelProperty("用户状态")
    private UserStatus userStatus;

    @ApiModelProperty("余额账户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    @ApiModelProperty("次数账户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long useAccountId;

    @ApiModelProperty("账户余额")
    private BigDecimal accountBalance;

    @ApiModelProperty("剩余次数")
    private Long useCountBalance;

    @ApiModelProperty("赠送次数")
    private Long giveCount;

    @ApiModelProperty("会员详情")
    private UserMemberDTO memberInfo;
}
