package com.quick.gpt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Longcm
 * @Description: gpt参数配置类
 */
@ConfigurationProperties(prefix = "chat")
@Data
public class ChatGptParamsConfig {
    /*
    chatgpt 配置
     */
    private String apiKey;
    private String host;
    private Integer port;
    private Integer tokens;

    /*
    tryleap.ai配置
     */
    private String tryleapHost;
    private String tryleapPath;
    private String tryleapKey;
    private String tryleapModels;
}
