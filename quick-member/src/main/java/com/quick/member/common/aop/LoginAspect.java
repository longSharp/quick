package com.quick.member.common.aop;

import cn.hutool.core.util.IdUtil;
import com.quick.member.common.enums.ResultCode;
import com.quick.member.domain.dto.req.AppSession;
import com.quick.member.domain.dto.resp.R;
import com.quick.member.domain.dto.resp.SysUserInfoRespDTO;
import com.quick.member.service.ISessionCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
public class LoginAspect {

    @Autowired
    private ISessionCache sessionRedisCache;

    @Around("execution(* com.quick.member.controller.UserController.checkSmsCode(..)) || execution(* com.quick.member.controller.UserController.loginByPwd(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        if (result instanceof R) {
            R responseBody=(R)result;
            if (!ResultCode.REQUEST_SUCCESS.getCode().equals(responseBody.getCode())) {
                // 接口未正常处理完成，不设置sessionId
                return result;
            }
            // 设置sessionId
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
            //请求的IP
            String ip = request.getRemoteAddr();
            SysUserInfoRespDTO data = (SysUserInfoRespDTO) responseBody.getData();
            String sessionId = IdUtil.simpleUUID()+"-"+data.getId();
            AppSession session = AppSession.create(ip,data.getId()+"",sessionId);
            //挤登
            sessionRedisCache.clearSessionForHash(data.getId()+"");
            sessionRedisCache.putSessionForHash(session);
            // 登入成功，设置sessionId到header
            response.setHeader("sessionId", sessionId);
        }
        return result;
    }
}
