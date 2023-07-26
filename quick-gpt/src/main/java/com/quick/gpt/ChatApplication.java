package com.quick.gpt;

import com.quick.common.config.BaiduParamsConfig;
import com.quick.common.config.ServiceParamsConfig;
import com.quick.gpt.annotation.EnableChatGPT;
import com.quick.gpt.config.ChatGptParamsConfig;
import com.quick.mongodb.repository.BaseSimpleMongoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.quick")
@EnableChatGPT
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages = {"com.quick"}, repositoryBaseClass = BaseSimpleMongoRepository.class)
@EnableConfigurationProperties({ServiceParamsConfig.class,
        ChatGptParamsConfig.class,
        BaiduParamsConfig.class})
@EnableFeignClients("com.quick.gpt.feign")
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class,args);
    }
}
