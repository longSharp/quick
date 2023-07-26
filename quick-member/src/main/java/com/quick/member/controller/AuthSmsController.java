package com.quick.member.controller;

import cn.hutool.core.util.RandomUtil;
import com.quick.common.dto.resp.R;
import com.quick.common.enums.ResultCode;
import com.quick.member.common.config.params.AuthSmsParamsConfig;
import com.quick.member.common.constant.AuthServerConstant;
import com.quick.member.domain.dto.resp.AuthSmsBaseResp;
import com.quick.member.domain.dto.resp.SendCodeRespDTO;
import com.quick.member.domain.dto.resp.SysUserInfoRespDTO;
import com.quick.member.service.IAuthSmsService;
import com.quick.member.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Longcm
 * @Description: 短信相关
 */
@RestController
@RequestMapping("/sms")
@Validated
public class AuthSmsController {
    @Autowired
    private IAuthSmsService authSmsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AuthSmsParamsConfig authSmsParamsConfig;

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping(value = "sendCode", method = RequestMethod.POST)
    public R<String> sendCode(
            @Pattern(regexp = "^1[3|4|5|7|8|9][0-9]\\d{8}$", message = "电话号码格式不正确") @NotBlank @RequestParam String phone){
        //接口防刷
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if(!StringUtils.isEmpty(redisCode)){
            long l = Long.parseLong(redisCode.split("_")[1]);
            if(System.currentTimeMillis()-l<Long.parseLong(authSmsParamsConfig.getMinute())*1000){
                //minute秒内不能再发送
                return R.error(ResultCode.SMS_CODE_EXCEPTION.getCode(),ResultCode.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        //检查用户是否已注册
        SysUserInfoRespDTO sysUserPO = sysUserService.queryUserByPhone(phone);
        SendCodeRespDTO data = new SendCodeRespDTO();
        data.setRegistered(sysUserPO!=null);
        String code = RandomUtil.randomNumbers(Integer.parseInt(authSmsParamsConfig.getLength()));
        data.setCode(code);
        String codeStr = code + "_" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phone,codeStr,10, TimeUnit.MINUTES);
//        AuthSmsBaseResp authSmsBaseResp = authSmsService.sendCode(phone, code.split("_")[0]);
        AuthSmsBaseResp authSmsBaseResp = AuthSmsBaseResp.builder().code(ResultCode.REQUEST_SUCCESS.getCode()).msg(ResultCode.REQUEST_SUCCESS.getMsg()).build();
        return R.ok(data);
    }
}
