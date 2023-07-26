package com.quick.member.jobs;

import com.quick.member.common.config.redis.MessageConsumer;
import com.quick.common.constant.RedisKeyPrefixConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MemberScheduler {
    @Autowired
    private MessageConsumer messageConsumer;

    /**
     * 处理会员(日)过期2分钟一巡检)
     */
    @Scheduled(fixedDelay=2*60*1000)
    public void day(){
        messageConsumer.consumerByMember(RedisKeyPrefixConstant.MEMBER_DAY_QUEUE_NAME);
    }

    /**
     * 处理会员(日)过期3分钟一巡检)
     */
    @Scheduled(fixedDelay=2*60*1000)
    public void week(){
        messageConsumer.consumerByMember(RedisKeyPrefixConstant.MEMBER_WEEK_QUEUE_NAME);
    }

    /**
     * 处理会员(日)过期4分钟一巡检)
     */
    @Scheduled(fixedDelay=2*60*1000)
    public void month(){
        messageConsumer.consumerByMember(RedisKeyPrefixConstant.MEMBER_MONTH_QUEUE_NAME);
    }

    /**
     * 处理会员(日)过期5分钟一巡检)
     */
    @Scheduled(fixedDelay=2*60*1000)
    public void quarter(){
        messageConsumer.consumerByMember(RedisKeyPrefixConstant.MEMBER_QUARTER_QUEUE_NAME);
    }

    /**
     * 处理会员(日)过期6分钟一巡检)
     */
    @Scheduled(fixedDelay=2*60*1000)
    public void year(){
        messageConsumer.consumerByMember(RedisKeyPrefixConstant.MEMBER_YEAR_QUEUE_NAME);
    }
}
