package com.quick.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.member.common.config.exception.BusinessException;
import com.quick.member.common.config.params.ServiceParamsConfig;
import com.quick.member.common.config.redis.MessageProducer;
import com.quick.member.common.constant.RedisKeyPrefixConstant;
import com.quick.member.common.enums.*;
import com.quick.member.dao.*;
import com.quick.member.domain.dto.req.RedisDelayMessage;
import com.quick.member.domain.po.*;
import com.quick.member.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * <p>
 * 订单记录表 服务实现类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderPO> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PayRecordMapper payRecordMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserMemberMapper userMemberMapper;

    @Autowired
    private ServiceParamsConfig config;

    @Autowired
    private UseAccountMapper useAccountMapper;

    @Autowired
    private UseLogMapper useLogMapper;

    @Autowired
    private MessageProducer productor;

    @Override
    public OrderPO getOrderByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderPO> orderPOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderPOLambdaQueryWrapper.eq(OrderPO::getOrderNo,orderNo);
        return orderMapper.selectOne(orderPOLambdaQueryWrapper);
    }

    @Override
    public void modifyOrderStatus(String orderNo, OrderStatus status) {
        OrderPO orderByOrderNo = getOrderByOrderNo(orderNo);
        orderByOrderNo.setOrderStatus(status);
        orderMapper.updateById(orderByOrderNo);
    }

    @Transactional
    @Override
    public void modifyOrderAndRecordStatus(String orderNo, OrderStatus status) {
        //1.更新订单状态
        OrderPO orderByOrderNo = getOrderByOrderNo(orderNo);
        if(orderByOrderNo==null){
            throw new BusinessException(ResultCode.PRODUCT_NOT_EXISTS);
        }
        orderByOrderNo.setOrderStatus(status);
        orderMapper.updateById(orderByOrderNo);

        //2.更新支付记录状态
        LambdaQueryWrapper<PayRecordPO> payRecordPOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        payRecordPOLambdaQueryWrapper.eq(PayRecordPO::getOrderNo,orderNo).eq(PayRecordPO::getStatus, Status.VALID);
        PayRecordPO payRecordPO = payRecordMapper.selectOne(payRecordPOLambdaQueryWrapper);
        payRecordPO.setPayStatus(status);
        payRecordMapper.updateById(payRecordPO);
        //只有支付成功时才执行后面逻辑
        if(OrderStatus.PAID!=status) return;

        //3.判断是次数购买还是会员充值
        if(orderByOrderNo.getProductType() != ProductType.COUNT_CARD){
            //4.若是会员充值,则给用户充值会员
            LambdaQueryWrapper<SysUserPO> sysUserPOLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysUserPOLambdaQueryWrapper.eq(SysUserPO::getId,orderByOrderNo.getUserId())
                    .eq(SysUserPO::getStatus,Status.VALID)
                    .eq(SysUserPO::getUserStatus,UserStatus.NORMAL);
            SysUserPO sysUserPO = new SysUserPO();
            sysUserPO.setIsMember(MemberMark.MEMBER);
            sysUserMapper.update(sysUserPO,sysUserPOLambdaQueryWrapper);

            UserMemberPO userMemberPO = new UserMemberPO();
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = getEndDate(start, orderByOrderNo.getProductType());
            //TODO 新增会员信息
            userMemberPO.setUserId(orderByOrderNo.getUserId())
                    .setType(orderByOrderNo.getProductType())
                    .setStartDate(start)
                    .setEndDate(end)
                    .setOrderNo(orderNo);
            userMemberMapper.insert(userMemberPO);

            //将信息加入延时队列
            RedisDelayMessage message = new RedisDelayMessage();
            message.setUserId(orderByOrderNo.getUserId());
            message.setOrderNo(orderByOrderNo.getOrderNo());
            long endMill = end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long startMill = start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            pushQueue(orderByOrderNo.getProductType(),message,endMill-startMill);
        }else{
            //5.若是次数充值，则更新次数账户，同时记录次数使用记录
            //更新次数账户
            QueryWrapper<UseAccountPO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",orderByOrderNo.getUserId()).eq("status", Status.VALID);
            UseAccountPO useAccount = useAccountMapper.selectOne(queryWrapper);
            useAccount.setBalanceCount(useAccount.getBalanceCount()+orderByOrderNo.getUseCount());
            useAccountMapper.updateById(useAccount);
            //生成记录
            UseLogPO useLog = new UseLogPO();
            useLog.setType(BalanceType.BRANCH_IN)
                    .setEvent(UseAccountEvent.CARD_PURCHASE)
                    .setUseAccountId(useAccount.getId())
                    .setCount(orderByOrderNo.getUseCount());
            useLogMapper.insert(useLog);
        }
    }

    @Transactional
    @Override
    public void cancelMember(Long userId) {
        LambdaQueryWrapper<SysUserPO> sysUserPOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserPOLambdaQueryWrapper.eq(SysUserPO::getId,userId)
                .eq(SysUserPO::getStatus,Status.VALID)
                .eq(SysUserPO::getUserStatus,UserStatus.NORMAL);
        SysUserPO sysUserPO = new SysUserPO();
        sysUserPO.setIsMember(MemberMark.UNMEMBER);
        sysUserMapper.update(sysUserPO,sysUserPOLambdaQueryWrapper);

        LambdaQueryWrapper<UserMemberPO> userMemberPOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userMemberPOLambdaQueryWrapper.eq(UserMemberPO::getUserId,userId).eq(UserMemberPO::getStatus,Status.VALID);
        UserMemberPO userMemberPO = new UserMemberPO();
        userMemberPO.setStatus(Status.INVALID);
        userMemberMapper.update(userMemberPO,userMemberPOLambdaQueryWrapper);
        log.info("{}用户会员到期！",userId);
    }


    /**
     * 获取会员结束时间
     * @param start 开始时间
     * @param type 会员类型
     * @return
     */
    private LocalDateTime getEndDate(LocalDateTime start,ProductType type){
        LocalDateTime end;
        switch (type){
            case MEMBER_DAY:
                end =  start.plusDays(1);
                break;
            case MEMBER_WEEK:
                end =  start.plusWeeks(1);
                break;
            case MEMBER_MONTH:
                end =  start.plusMonths(1);
                break;
            case MEMBER_QUARTER:
                end =  start.plusMonths(3);
                break;
            case MEMBER_YEAR:
                end =  start.plusYears(1);
                break;
            default:
                end =  start.plusYears(100);
                break;
        }
        return end;
    }

    private void pushQueue(ProductType type, RedisDelayMessage message, long time){
        String queueName;
        switch (type){
            case MEMBER_DAY:
                queueName = RedisKeyPrefixConstant.MEMBER_DAY_QUEUE_NAME;
                break;
            case MEMBER_WEEK:
                queueName = RedisKeyPrefixConstant.MEMBER_WEEK_QUEUE_NAME;
                break;
            case MEMBER_MONTH:
                queueName = RedisKeyPrefixConstant.MEMBER_MONTH_QUEUE_NAME;
                break;
            case MEMBER_QUARTER:
                queueName = RedisKeyPrefixConstant.MEMBER_QUARTER_QUEUE_NAME;
                break;
            default:
                queueName = RedisKeyPrefixConstant.MEMBER_YEAR_QUEUE_NAME;
                break;
        }
        productor.pushMessage(message, queueName,time);
    }
}
