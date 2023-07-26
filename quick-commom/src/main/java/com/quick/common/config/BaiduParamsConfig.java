package com.quick.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "baidu")
@Data
public class BaiduParamsConfig {
    private String appId;
    private String securityKey;
    private String transApiHost;
}
