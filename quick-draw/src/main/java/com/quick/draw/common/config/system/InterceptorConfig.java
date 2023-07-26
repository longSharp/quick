package com.quick.draw.common.config.system;

import com.quick.common.inteceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    private final String[] excludePathPatterns = new String[]{
            "/user/loginByPwd",
            "/user/checkSmsCode",
            "/sms/sendCode",
            "/user/modifyPwd",
            "/gpt/sendStream",
            "/payment/notify",
            "/user/touristLogin",
            "/user/checkLogin"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(excludePathPatterns); // 不拦截登录请求
    }
}
