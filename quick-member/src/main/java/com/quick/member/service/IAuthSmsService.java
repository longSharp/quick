package com.quick.member.service;


import com.quick.member.domain.dto.resp.AuthSmsBaseResp;

/**
 * @Author: Longcm
 * @Description: 短信相关业务
 */
public interface IAuthSmsService {
    /**
     * 发送验证码
     * @param phone 电话号码
     * @param code 验证码
     * @return
     */
    AuthSmsBaseResp sendCode(String phone, String code);

    /**
     * 新版发送验证码
     * @param phone 电话号码
     * @param code 验证码
     * @return
     */
    AuthSmsBaseResp sendCodeNew(String phone, String code) throws Exception;
}
