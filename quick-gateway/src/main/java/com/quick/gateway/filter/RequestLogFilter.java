package com.quick.gateway.filter;

import com.quick.common.dto.req.RequestLogContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 请求日志输出
 */
@Slf4j
@Component
public class RequestLogFilter implements GlobalFilter, Ordered {

    private static final String START_TIME_KEY = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        RequestLogContext requestLogContext = new RequestLogContext();
        exchange.getAttributes().put(START_TIME_KEY, System.currentTimeMillis());
        ServerHttpRequest request = exchange.getRequest();
        String method = Objects.requireNonNull(request.getMethod()).name();
        List<String> list = request.getHeaders().get("sessionId");
        String sessionId = list==null?null:list.size()<1?null:list.get(0);
        String requestURI = request.getPath().toString();
        String remoteAddr = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        Set<Map.Entry<String, List<String>>> entries = queryParams.entrySet();
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            builder.append(key).append("=").append(value);
            builder.append(",");
        }
        builder.append("}");
        requestLogContext.setRequestMethod(method);
        requestLogContext.setRequestQuery(builder.toString());
        requestLogContext.setRequestUrl(requestURI);
        requestLogContext.setRemoteAddr(remoteAddr);
        requestLogContext.setRequestSessionId(sessionId);
        exchange.getAttributes().put("log",requestLogContext);
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME_KEY);
            RequestLogContext requestLog = exchange.getAttribute("log");
            long duration = System.currentTimeMillis() - startTime;
            assert requestLog != null;
            requestLog.setRequestDuration(duration+"ms");
            log.info(requestLog.toString());
        }));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
