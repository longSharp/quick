package com.quick.member.common.config.params;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "baidu")
@Data
public class BaiduParamsConfig {
    private String appId;
    private String securityKey;
    private String transApiHost;
}
