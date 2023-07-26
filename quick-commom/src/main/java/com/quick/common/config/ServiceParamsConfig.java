package com.quick.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

/**
 * @Author: Longcm
 * @Description: 业务参数配置类
 */
@ConfigurationProperties(prefix = "service")
@Data
public class ServiceParamsConfig {
    /**
     * 邀请奖励费用(元)
     */
    private BigDecimal invitFee;

    /**
     * 邀请奖励次数(次)
     */
    private Long invitUseCount;

    /**
     * 注册初始化费用(元)
     */
    private BigDecimal initFee;

    /**
     * 注册初始化次数(次)
     */
    private Long initUseCount;

    /**
     * 邀请码长度
     */
    private Integer invitLength;

    /**
     * 登入超时时间(分)
     */
    private Long loginLockTime;

    /**
     * 签到奖励次数
     */
    private Long attendanceCount;

    /**
     * 会员每日使用次数上限
     */
    private Long memberUseCount;

    /**
     * 订单超时时间(分钟)
     */
    private Long orderTimeOut;

    /**
     * minio baseUrl
     */
    private String minioBaseUrl;

    /**
     * 加载主题数据文件路径
     */
    private String loadTopicPath;
}
