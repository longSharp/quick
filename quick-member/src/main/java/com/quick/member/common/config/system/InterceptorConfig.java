package com.quick.member.common.config.system;

import com.quick.member.common.interceptor.CommonInterceptor;
import com.quick.member.common.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private CommonInterceptor commonInterceptor;

    private final String[] excludePathPatterns = new String[]{
            "/user/loginByPwd",
            "/user/checkSmsCode",
            "/sms/sendCode",
            "/user/modifyPwd",
            "/gpt/sendStream",
            "/payment/notify",
            "/image/generate",
            "/image/getTask",
            "/types/getEnum/imageProport"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 通用拦截器
        registry.addInterceptor(commonInterceptor)
                .addPathPatterns("/**"); // 拦截所有请求
        // 注册拦截器
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(excludePathPatterns); // 不拦截登录请求
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(3600);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
