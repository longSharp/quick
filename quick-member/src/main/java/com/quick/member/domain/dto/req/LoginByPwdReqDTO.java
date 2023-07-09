package com.quick.member.domain.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginByPwdReqDTO {
    /**
     * 手机号
     */
    @NotBlank
    @Pattern(regexp = "^1[3|4|5|7|8|9][0-9]\\d{8}$", message = "电话号码格式不正确")
    private String phone;
    /**
     * 密码
     */
    @NotBlank
    private String password;
}
