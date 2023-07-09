package com.quick.member.common.config.params;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Author: Longcm
 * @Description: 短信参数配置类
 */
@ConfigurationProperties(prefix = "sms")
@Data
public class AuthSmsParamsConfig {
    /**
     * 短信发送验证码host
     */
    private String host;

    /**
     * 发送验证路径
     */
    private String path;

    /**
     * 阿里巴巴订购的短信产品appCode
     */
    private String appCode;

    /**
     * 短信有效时间(秒)
     */
    private String minute;

    /**
     * 短信验证码长度
     */
    private String length;

    /**
     * 短信前缀ID（签名ID）
     */
    private String smsSignId;

    /**
     * 短信正文ID（模板ID）
     */
    private String templateId;

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * securityToken
     */
    private String securityToken;
}
