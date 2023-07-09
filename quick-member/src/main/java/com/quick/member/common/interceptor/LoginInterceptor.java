package com.quick.member.common.interceptor;

import cn.hutool.json.JSONUtil;
import com.quick.member.common.enums.ResultCode;
import com.quick.member.domain.dto.req.AppSession;
import com.quick.member.domain.dto.resp.R;
import com.quick.member.service.ISessionCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private ISessionCache sessionRedisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId = request.getHeader("sessionId");
        String requestURI = request.getRequestURI();
        String loginOut = "loginOut";
        if(StringUtils.isEmpty(sessionId)){
            R<Object> error = R.error(ResultCode.SESSION_EMPTY.getCode(),ResultCode.SESSION_EMPTY.getMsg());
            if(requestURI.contains(loginOut)){
                error = R.ok(ResultCode.REQUEST_SUCCESS.getCode(),ResultCode.REQUEST_SUCCESS.getMsg());
            }
            String json = JSONUtil.toJsonStr(error);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
            return false;
        }
        String remoteAddr = request.getRemoteAddr();
        String[] userIds = sessionId.split("-");
        if(userIds.length<2){
            R<Object> error = R.error(ResultCode.LOGIN_TIME_OUT.getCode(), ResultCode.LOGIN_TIME_OUT.getMsg());
            if(requestURI.contains(loginOut)){
                error = R.ok(ResultCode.REQUEST_SUCCESS.getCode(),ResultCode.REQUEST_SUCCESS.getMsg());
            }
            String json = JSONUtil.toJsonStr(error);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
            return false;
        }
        AppSession sessionObj = sessionRedisCache.getSessionForHash(userIds[1], remoteAddr);
        if(sessionObj==null||!sessionObj.getId().equals(sessionId)){
            R<Object> error = R.error(ResultCode.LOGIN_TIME_OUT.getCode(), ResultCode.LOGIN_TIME_OUT.getMsg());
            if(requestURI.contains(loginOut)){
                error = R.ok(ResultCode.REQUEST_SUCCESS.getCode(),ResultCode.REQUEST_SUCCESS.getMsg());
            }
            String json = JSONUtil.toJsonStr(error);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
            return false;
        }
        return true;
    }
}
