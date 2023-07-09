package com.quick.member.common.config.redis;

import com.quick.member.domain.dto.req.RedisDelayMessage;
import com.quick.member.service.IMessageDelayQueueService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Longcm
 * @Description: 消息生产者
 */
@Component
public class MessageProducer {
    @Autowired
    IMessageDelayQueueService delayService;

    /**
     * 发送数据
     * @param message 消息
     */
    @SneakyThrows
    public Boolean pushMessage(RedisDelayMessage message){
        return delayService.pushMessage(message);
    }

    /**
     * 发送数据
     * @param message 消息
     */
    @SneakyThrows
    public Boolean pushMessage(RedisDelayMessage message, String delayQueueName, long time){
        return delayService.pushMessage(message,delayQueueName,time);
    }
}
