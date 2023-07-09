package com.quick.member.service.impl;

import cn.hutool.json.JSONUtil;
import com.quick.member.common.config.params.ServiceParamsConfig;
import com.quick.member.common.constant.RedisKeyPrefixConstant;
import com.quick.member.domain.dto.req.RedisDelayMessage;
import com.quick.member.service.IMessageDelayQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageDelayQueueServiceImpl implements IMessageDelayQueueService {

    @Autowired
    private ServiceParamsConfig config;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public Boolean pushMessage(RedisDelayMessage message) {
        long score = System.currentTimeMillis() + config.getOrderTimeOut() * 60 * 1000;
        String msg = JSONUtil.toJsonStr(message);
        return redisTemplate.opsForZSet().add(RedisKeyPrefixConstant.DELAY_QUEUE_NAME,msg,score);
    }

    @Override
    public Boolean pushMessage(RedisDelayMessage message, String delayQueueName) {
        long score = System.currentTimeMillis() + config.getOrderTimeOut() * 60 * 1000;
        String msg = JSONUtil.toJsonStr(message);
        return redisTemplate.opsForZSet().add(delayQueueName,msg,score);
    }

    @Override
    public Boolean pushMessage(RedisDelayMessage message, String delayQueueName, long time) {
        long score = System.currentTimeMillis() + time;
        String msg = JSONUtil.toJsonStr(message);
        return redisTemplate.opsForZSet().add(delayQueueName,msg,score);
    }

    @Override
    public List<RedisDelayMessage> pull() {
        return pull(RedisKeyPrefixConstant.DELAY_QUEUE_NAME);
    }

    @Override
    public List<RedisDelayMessage> pull(String delayQueueName) {
        List<RedisDelayMessage> redisDelayMessages = new ArrayList<>();
        try {
            long curTime = System.currentTimeMillis();
            Set<String> msgs = redisTemplate.opsForZSet().rangeByScore(delayQueueName, 0, curTime);
            if(msgs==null){
                return null;
            }
            redisDelayMessages = msgs.stream().map(msg->{
                RedisDelayMessage message = null;
                try {
                    message = JSONUtil.toBean(msg, RedisDelayMessage.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return message;

            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return redisDelayMessages;
    }

    @Override
    public Boolean remove(RedisDelayMessage message) {
        return remove(message,RedisKeyPrefixConstant.DELAY_QUEUE_NAME);
    }

    @Override
    public Boolean remove(RedisDelayMessage message, String delayQueueName) {
        Long remove = redisTemplate.opsForZSet().remove(delayQueueName, JSONUtil.toJsonStr(message));
        return remove!=null&&remove>0;
    }
}
