package com.quick.member.common.interceptor;

import com.quick.member.common.config.system.ContentCachingRequestWrapper;
import com.quick.member.domain.dto.req.RequestLogContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Deprecated
@Slf4j
@Component
public class CommonInterceptor implements HandlerInterceptor {

    private final ThreadLocal<RequestLogContext> logContextThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<Long> start = new ThreadLocal<>();
    private final ThreadLocal<Long> end = new ThreadLocal<>();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        start.set(System.currentTimeMillis());
        RequestLogContext requestLogContext = new RequestLogContext();
        String sessionId = request.getHeader("sessionId");
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String remoteAddr = request.getRemoteAddr();
        String body = null;
        if(HttpMethod.POST.matches(method)&&!requestURI.contains("imageGenerate")){
            if(request instanceof ContentCachingRequestWrapper){
                ContentCachingRequestWrapper requestWapper = (ContentCachingRequestWrapper) request;
                body = IOUtils.toString(requestWapper.getBody(), requestWapper.getCharacterEncoding());
            }
        }
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        while (parameterNames.hasMoreElements()){
            String name = parameterNames.nextElement();
            builder.append(name+"="+request.getParameter(name)+",");
        }
        builder.append("}");
        requestLogContext.setRequestBody(body)
                        .setRequestQuery(builder.toString())
                        .setRemoteAddr(remoteAddr)
                .setRequestMethod(method)
                .setRequestUrl(requestURI)
                .setRequestSessionId(sessionId);
        logContextThreadLocal.set(requestLogContext);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        end.set(System.currentTimeMillis());
        RequestLogContext requestLogContext = logContextThreadLocal.get();
        requestLogContext.setRequestDuration(end.get()-start.get()+"ms");
        log.info(requestLogContext.toString());
        start.remove();
        end.remove();
        logContextThreadLocal.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
