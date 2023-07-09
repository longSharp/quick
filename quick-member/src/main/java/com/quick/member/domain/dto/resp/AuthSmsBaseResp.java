package com.quick.member.domain.dto.resp;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @Author: Longcm
 * @Description: 短信验证响应dto
 */
@Data
@ToString
@Builder
public class AuthSmsBaseResp {
    private String code;
    private String msg;
}
