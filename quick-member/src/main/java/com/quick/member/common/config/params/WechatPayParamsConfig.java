package com.quick.member.common.config.params;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Longcm
 * @Description: 微信支付参数配置类
 */
@ConfigurationProperties(prefix = "wechat")
@Data
public class WechatPayParamsConfig {
    /**
     * 商户id
     */
    private String merchantId;
    /**
     * 公众号appid(和商户id绑定过)
     */
    private String appId;
    /**
     * 商户私钥
     */
    private String privateKey;
    /**
     * APIv3密钥(在微信支付回调通知和商户获取平台证书使用APIv3密钥)
     */
    private String apiV3Key;
    /**
     * 必填：APIv2密钥（调用v2版本的API时，需用APIv2密钥生成签名）
     */
    private String mchKey;
    /**
     * 必填：apiclient_cert.p12证书文件的绝对路径，或者以classpath:开头的类路径。
     */
    private String keyPath;

    /**
     * 必填：apiclient_key.pem证书文件的绝对路径，或者以classpath:开头的类路径。
     */
    private String privateKeyPath;

    /**
     * 必填：apiclient_cert.pem证书文件的绝对路径，或者以classpath:开头的类路径。
     */
    private String privateCertPath;
    /**
     * 接收结果通知地址
     */
    private String notifyDomain;
}
