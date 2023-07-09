package com.quick.member.common.config.redis;

import com.quick.member.common.constant.RedisKeyPrefixConstant;
import com.quick.member.common.enums.OrderStatus;
import com.quick.member.common.enums.Status;
import com.quick.member.common.enums.WechatPayMentStatus;
import com.quick.member.domain.dto.req.RedisDelayMessage;
import com.quick.member.domain.po.OrderPO;
import com.quick.member.service.IMessageDelayQueueService;
import com.quick.member.service.OrderService;
import com.quick.member.service.WechatPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Longcm
 * @Description: 消费者
 */
@Slf4j
@Component
public class MessageConsumer {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IMessageDelayQueueService delayQueueService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WechatPaymentService wechatPaymentService;

    public void consumerByOrder(String delayQueueName){
        RLock lock = null;
        try {
            //加分布式锁
            lock = redissonClient.getLock(RedisKeyPrefixConstant.LOCAK_ORDER_PREFIX+ delayQueueName);
            boolean isLock = lock.tryLock();
            if(isLock) {
                if(RedisKeyPrefixConstant.DELAY_QUEUE_NAME.equals(delayQueueName)){
                    List<RedisDelayMessage> messageList = delayQueueService.pull();
                    handleMessageForOrder(messageList);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(lock!=null){
                lock.unlock();
            }
        }
    }
    public void consumerByMember(String delayQueueName){
        RLock lock = null;
        try {
            //加分布式锁
            lock = redissonClient.getLock(RedisKeyPrefixConstant.LOCAK_MEMBER_PREFIX+ delayQueueName);
            boolean isLock = lock.tryLock();
            if(isLock) {
                List<RedisDelayMessage> messageList = delayQueueService.pull(delayQueueName);
                handleMessageForMember(messageList,delayQueueName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(lock!=null){
                lock.unlock();
            }
        }
    }



    /**
     * 检查超时订单
     * @param messageList
     */
    public void  handleMessageForOrder( List<RedisDelayMessage> messageList){
        for (RedisDelayMessage message : messageList) {
            OrderPO orderByOrderNo = orderService.getOrderByOrderNo(message.getOrderNo());
            if(orderByOrderNo==null || orderByOrderNo.getStatus()!= Status.VALID|| orderByOrderNo.getOrderStatus()!= OrderStatus.WAIT_PAY){
                delayQueueService.remove(message,RedisKeyPrefixConstant.DELAY_QUEUE_NAME);
                continue;
            }
            if(orderByOrderNo.getOrderStatus()== OrderStatus.WAIT_PAY){
                String orderNo = orderByOrderNo.getOrderNo();
                WechatPayMentStatus wechatPayMentStatus = wechatPaymentService.queryOrderForWechat(orderNo);
                if(wechatPayMentStatus==null) continue;
                switch (wechatPayMentStatus){
                    case SUCCESS:
                        orderService.modifyOrderAndRecordStatus(orderNo,OrderStatus.PAID);
                        break;
                    case NOTPAY:
                        wechatPaymentService.closeOrder(orderNo);
                        break;
                }
                delayQueueService.remove(message,RedisKeyPrefixConstant.DELAY_QUEUE_NAME);
            }
        }
    }

    /**
     * 检查超时会员
     */
    public void  handleMessageForMember( List<RedisDelayMessage> messageList,String queueName){
        for (RedisDelayMessage message : messageList) {
            orderService.cancelMember(message.getUserId());
            delayQueueService.remove(message,queueName);
        }
    }

}
