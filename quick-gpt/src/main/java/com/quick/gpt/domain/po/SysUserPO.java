package com.quick.gpt.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.quick.gpt.enums.MemberMark;
import com.quick.gpt.enums.UserRole;
import com.quick.gpt.enums.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "用户表")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class SysUserPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户名")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("用户角色")
    @TableField("`role`")
    private UserRole role;

    @ApiModelProperty("手机号")
    @TableField("tel")
    private String tel;

    @ApiModelProperty("密码")
    @TableField("`password`")
    private String password;

    @ApiModelProperty("是否是会员")
    @TableField("is_member")
    private MemberMark isMember;

    @ApiModelProperty("邀请码")
    @TableField("invite_code")
    private String inviteCode;

    @ApiModelProperty("邀请人邀请码")
    @TableField("invite_person_code")
    private String invitePersonCode;

    @ApiModelProperty("头像")
    @TableField("profile_photo")
    private String profilePhoto;

    @ApiModelProperty("用户状态")
    @TableField("user_status")
    private UserStatus userStatus;

    @ApiModelProperty("用户注册ip")
    @TableField("user_ip")
    private String userIp;
}
