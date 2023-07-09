package com.quick.gpt.config;

import com.quick.gpt.service.OpenAiProxyService;
import com.quick.gpt.util.OpenAiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(ChatGPTProperties.class)
public class ChatGPTAutoConfigure {
    @Autowired
    private ChatGPTProperties properties;

    @Bean
    public OpenAiProxyService openAiProxyService() {
        return new OpenAiProxyService(properties, Duration.ZERO);
    }

    @Bean
    public OpenAiUtils openAiUtils(OpenAiProxyService openAiService) {
        return new OpenAiUtils(openAiService);
    }
}
