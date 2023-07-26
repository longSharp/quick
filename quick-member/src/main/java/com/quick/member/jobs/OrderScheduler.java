package com.quick.member.jobs;

import com.quick.member.common.config.redis.MessageConsumer;
import com.quick.common.constant.RedisKeyPrefixConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderScheduler {
    @Autowired
    private MessageConsumer messageConsumer;

    /**
     * 处理超时订单(一分钟一巡检)
     */
    @Scheduled(fixedDelay=60*1000)
    public void orderTimeOut(){
        messageConsumer.consumerByOrder(RedisKeyPrefixConstant.DELAY_QUEUE_NAME);
    }
}
