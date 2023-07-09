package com.quick.member.service;

import com.quick.member.domain.dto.req.RedisDelayMessage;

import java.util.List;

/**
 * @Author: Longcm
 * @Description:
 */
public interface IMessageDelayQueueService {
    /**
     * 发送数据
     * @param message 消息
     */
    Boolean pushMessage(RedisDelayMessage message);

    /**
     * 发送数据
     * @param message 消息
     */
    Boolean pushMessage(RedisDelayMessage message, String delayQueueName);

    /**
     * 发送数据
     * @param message 消息
     */
    Boolean pushMessage(RedisDelayMessage message, String delayQueueName, long time);

    /**
     * 拉取最新需要
     * 被消费的消息
     * rangeByScore 根据score范围获取 0-当前时间戳可以拉取当前时间及以前的需要被消费的消息
     *
     * @return
     */
    List<RedisDelayMessage> pull();

    /**
     * 拉取最新需要
     * 被消费的消息
     * rangeByScore 根据score范围获取 0-当前时间戳可以拉取当前时间及以前的需要被消费的消息
     *
     * @return
     */
    List<RedisDelayMessage> pull(String delayQueueName);

    /**
     * 移除消息
     *
     * @param message
     */
    Boolean remove(RedisDelayMessage message);

    /**
     * 移除消息
     *
     * @param message
     */
    Boolean remove(RedisDelayMessage message, String delayQueueName);

}
