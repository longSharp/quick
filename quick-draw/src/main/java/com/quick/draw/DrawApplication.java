package com.quick.draw;

import com.quick.common.config.BaiduParamsConfig;
import com.quick.common.config.ServiceParamsConfig;
import com.quick.draw.common.config.params.MidjourneyParamsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.quick")
@EnableConfigurationProperties({MidjourneyParamsConfig.class, BaiduParamsConfig.class, ServiceParamsConfig.class})
public class DrawApplication {
    public static void main(String[] args) {
        SpringApplication.run(DrawApplication.class,args);
    }
}
