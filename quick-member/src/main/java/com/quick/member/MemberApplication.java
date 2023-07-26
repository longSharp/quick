package com.quick.member;

import com.quick.common.config.BaiduParamsConfig;
import com.quick.member.common.config.params.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.quick.member")
@EnableAspectJAutoProxy
@EnableScheduling
@EnableDiscoveryClient
@EnableConfigurationProperties({AuthSmsParamsConfig.class,
        ServiceParamsConfig.class,
        WechatPayParamsConfig.class,
        BaiduParamsConfig.class})
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }

}
