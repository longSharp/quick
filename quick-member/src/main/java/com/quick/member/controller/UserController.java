package com.quick.member.controller;

import com.quick.common.dto.resp.R;
import com.quick.common.enums.ResultCode;
import com.quick.common.exception.BusinessException;
import com.quick.member.common.constant.AuthServerConstant;
import com.quick.member.common.enums.UserRole;
import com.quick.member.common.enums.UserStatus;
import com.quick.member.common.utils.UserHolder;
import com.quick.member.domain.dto.req.AppSession;
import com.quick.member.domain.dto.req.LoginByPwdReqDTO;
import com.quick.member.domain.dto.req.PasswordModiReqDTO;
import com.quick.member.domain.dto.resp.SysUserInfoRespDTO;
import com.quick.member.domain.dto.resp.UserCheckLoginRespDTO;
import com.quick.member.domain.po.SysUserPO;
import com.quick.member.domain.po.UseLogPO;
import com.quick.member.service.ISessionCache;
import com.quick.member.service.SysUserService;
import com.quick.member.service.UseLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ISessionCache sessionRedisCache;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UseLogService useLogService;

    @RequestMapping(value = "checkSmsCode", method = RequestMethod.POST)
    public R<String> checkSmsCode(
            @NotBlank @Pattern(regexp = "^1[3|4|5|7|8|9][0-9]\\d{8}$", message = "电话号码格式不正确") @RequestParam String phone,
            @NotBlank @RequestParam String code,
            @RequestParam(required = false) String invitCode){
        if(checkSms(phone,code)){
            //校验通过，删除验证码缓存
            //redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
            //检查用户是否已注册
            SysUserInfoRespDTO sysUserPO = sysUserService.queryUserByPhone(phone);
            //若用户已注册，则直接登入,否则创建用户
            if(sysUserPO!=null){
                return R.ok(sysUserPO);
            }
            //检查邀请码是否存在
            if(!checkInviCode(invitCode)){
                return R.error(ResultCode.INVIT_CODE_NOT_EXISTS.getCode(),ResultCode.INVIT_CODE_NOT_EXISTS.getMsg());
            }
            //创建用户
            try {
                SysUserInfoRespDTO us = sysUserService.createUser(phone,invitCode);
                return R.ok(us);
            } catch (Exception e) {
                e.printStackTrace();
                return R.error(ResultCode.REGISTER_FAIL.getCode(),ResultCode.REGISTER_FAIL.getMsg());
            }
        }
        return R.error(ResultCode.SMS_CODE_VALID_FAIL.getCode(),ResultCode.SMS_CODE_VALID_FAIL.getMsg());
    }

    @RequestMapping(value = "touristLogin", method = RequestMethod.POST)
    public R<String> touristRegister(HttpServletRequest request){
        String ip = request.getRemoteAddr();
        SysUserInfoRespDTO user = sysUserService.queryUserByIp(UserRole.TOURIST, ip);
        if(user!=null){
            return R.ok(user);
        }
        SysUserInfoRespDTO sysUserPO = sysUserService.touristRegister(ip);
        if(sysUserPO == null){
            return R.error();
        }
        return R.ok(sysUserPO);
    }

    @RequestMapping(value = "checkLogin", method = RequestMethod.POST)
    public R<String> isLogin(HttpServletRequest request){
        boolean login = checkLogin(request);
        UserCheckLoginRespDTO userCheckLoginRespDTO = new UserCheckLoginRespDTO();
        userCheckLoginRespDTO.setLogin(login);
        return R.ok(userCheckLoginRespDTO);
    }

    @RequestMapping(value = "loginByPwd", method = RequestMethod.POST)
    public R<String> loginByPwd(@Valid @NotNull @RequestBody LoginByPwdReqDTO loginByPwdReqDTO){
        SysUserInfoRespDTO sysUserPO = sysUserService.checkPwd(loginByPwdReqDTO.getPhone(), loginByPwdReqDTO.getPassword());
        if(sysUserPO == null){
            return R.error(ResultCode.PASSWORD_ERROR.getCode(), ResultCode.PASSWORD_ERROR.getMsg());
        }
        return R.ok(sysUserPO);
    }

    @RequestMapping(value = "loginOut", method = RequestMethod.POST)
    public R loginOut(HttpServletRequest request){
        String sessionId = request.getHeader("sessionId");
        String[] userIds = sessionId.split("-");
        if(userIds.length<2){
            return R.error(ResultCode.LOGIN_OUT_FAIL.getCode(), ResultCode.LOGIN_OUT_FAIL.getMsg());
        }
        sessionRedisCache.clearSessionForHash(userIds[1]);
        return R.ok();

    }

    @RequestMapping(value = "attendance", method = RequestMethod.POST)
    public R<String> attendance(){
        Long userId = UserHolder.getUserId();
        //检测用户状态，角色
        checkUser(userId);
        //检查用户是否已签到
        useLogService.checkAttendence(userId);
        //进行签到
        UseLogPO attendance = useLogService.attendance(userId);
        return R.ok(attendance);

    }

    @RequestMapping(value = "modifyPwd", method = RequestMethod.POST)
    public R<String> modifyPwd(@Valid @NotNull @RequestBody PasswordModiReqDTO loginByPwdReqDTO){
        if(!checkSms(loginByPwdReqDTO.getPhone(), loginByPwdReqDTO.getCode())){
            return R.error(ResultCode.SMS_CODE_VALID_FAIL.getCode(),ResultCode.SMS_CODE_VALID_FAIL.getMsg());
        }
        SysUserPO sysUserPO = sysUserService.modifyPwd(loginByPwdReqDTO.getPhone(), loginByPwdReqDTO.getPassword());
        if(sysUserPO == null){
            return R.error(ResultCode.PASSWORD_MODIFY_FAIL.getCode(), ResultCode.PASSWORD_MODIFY_FAIL.getMsg());
        }
        SysUserInfoRespDTO sysUserInfoRespDTO = sysUserService.queryUserByPhone(loginByPwdReqDTO.getPhone());
        return R.ok(sysUserInfoRespDTO);
    }

    @RequestMapping(value = "getUserInfo", method = RequestMethod.POST)
    public R<String> getUserInfo(){
        SysUserInfoRespDTO sysUserInfoRespDTO = sysUserService.queryUserById(UserHolder.getUserId());
        return R.ok(sysUserInfoRespDTO);
    }

    private void checkUser(Long userId){
        SysUserInfoRespDTO sysUserInfoRespDTO = sysUserService.queryUserById(userId);
        if(sysUserInfoRespDTO==null){
            throw new BusinessException(ResultCode.USER_NOT_EXISTS);
        }
        UserStatus userStatus = sysUserInfoRespDTO.getUserStatus();
        if(!userStatus.getCode().equals(UserStatus.NORMAL.getCode())){
            throw new BusinessException(ResultCode.USE_STATUS_ERROR);
        }
        UserRole role = sysUserInfoRespDTO.getRole();
        if(role.getCode().equals(UserRole.TOURIST.getCode())){
            throw new BusinessException(ResultCode.TOURIST_NOT_ATT);
        }
    }

    private boolean checkInviCode(String code){
        if(StringUtils.isEmpty(code)){
            return true;
        }
        SysUserPO sysUserPO = sysUserService.queryUserByInviCode(code);
        return sysUserPO!=null;
    }

    private boolean checkSms(String phone,String code){
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        return !StringUtils.isEmpty(redisCode)&&code.equals(redisCode.split("_")[0]);
    }

    private boolean checkLogin(HttpServletRequest request){
        String sessionId = request.getHeader("sessionId");
        if(StringUtils.isEmpty(sessionId)){
            return false;
        }
        String remoteAddr = request.getRemoteAddr();
        String[] userIds = sessionId.split("-");
        if(userIds.length<2){
            return false;
        }
        AppSession sessionObj = sessionRedisCache.getSessionForHash(userIds[1], remoteAddr);
        return sessionObj != null && sessionObj.getId().equals(sessionId);
    }

    @GetMapping("getById")
    public R<String> getById(@RequestParam Long userId){
        SysUserPO user = sysUserService.getById(userId);
        return R.ok(user);
    }
}
