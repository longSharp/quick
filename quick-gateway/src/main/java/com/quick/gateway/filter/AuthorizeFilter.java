package com.quick.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.quick.common.dto.req.AppSession;
import com.quick.common.dto.resp.R;
import com.quick.common.enums.ResultCode;
import com.quick.gateway.service.ISessionCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 做请求拦截，是否登入
 */
@Slf4j
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    @Autowired
    private ISessionCache sessionRedisCache;

    //到map里的请求不需要做sessionId校验
    private static final Map<String,String> EXCLUDEPATHPATTERNS = new HashMap<>();

    static {
        EXCLUDEPATHPATTERNS.put("/member-service/user/touristLogin","");
        EXCLUDEPATHPATTERNS.put("/member-service/user/loginByPwd","");
        EXCLUDEPATHPATTERNS.put("/member-service/user/checkSmsCode","");
        EXCLUDEPATHPATTERNS.put("/member-service/sms/sendCode","");
        EXCLUDEPATHPATTERNS.put("/member-service/user/modifyPwd","");
        EXCLUDEPATHPATTERNS.put("/member-service/payment/notify","");
        EXCLUDEPATHPATTERNS.put("/member-service/user/checkLogin","");
        EXCLUDEPATHPATTERNS.put("/member-service/user/loginOut","");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        List<String> list = request.getHeaders().get("sessionId");
        String requestURI = request.getPath().toString();
        if(EXCLUDEPATHPATTERNS.containsKey(requestURI)){
            return chain.filter(exchange);
        }
        if(list==null||list.size()<1||StringUtils.isEmpty(list.get(0))){
            R<Object> error = R.error(ResultCode.SESSION_EMPTY.getCode(),ResultCode.SESSION_EMPTY.getMsg());
            String json = JSONUtil.toJsonStr(error);
            response.setStatusCode(HttpStatus.OK);
            DataBuffer wrap = response.bufferFactory().wrap(json.getBytes());
            return response.writeWith(Mono.just(wrap));
        }
        String sessionId = list.get(0);
        String remoteAddr = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
        String[] userIds = sessionId.split("-");
        if(userIds.length<2){
            R<Object> error = R.error(ResultCode.LOGIN_TIME_OUT.getCode(), ResultCode.LOGIN_TIME_OUT.getMsg());
            String json = JSONUtil.toJsonStr(error);
            response.setStatusCode(HttpStatus.OK);
            DataBuffer wrap = response.bufferFactory().wrap(json.getBytes());
            return response.writeWith(Mono.just(wrap));
        }
        AppSession sessionObj = sessionRedisCache.getSessionForHash(userIds[1], remoteAddr);
        if(sessionObj==null||!sessionObj.getId().equals(sessionId)){
            R<Object> error = R.error(ResultCode.LOGIN_TIME_OUT.getCode(), ResultCode.LOGIN_TIME_OUT.getMsg());
            String json = JSONUtil.toJsonStr(error);
            response.setStatusCode(HttpStatus.OK);
            DataBuffer wrap = response.bufferFactory().wrap(json.getBytes());
            return response.writeWith(Mono.just(wrap));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
