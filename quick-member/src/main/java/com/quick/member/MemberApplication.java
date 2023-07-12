package com.quick.member;

import com.quick.gpt.annotation.EnableChatGPT;
import com.quick.member.common.config.params.*;
import com.quick.mongodb.repository.BaseSimpleMongoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.quick.member")
@EnableAspectJAutoProxy
@EnableChatGPT
@EnableScheduling
@EnableMongoRepositories(basePackages = {"com.quick"}, repositoryBaseClass = BaseSimpleMongoRepository.class)
@EnableConfigurationProperties({AuthSmsParamsConfig.class,
        ServiceParamsConfig.class,
        ChatGptParamsConfig.class,
        WechatPayParamsConfig.class,
        MidjourneyParamsConfig.class,
        BaiduParamsConfig.class})
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }

}
