package com.quick.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.enums.Status;
import com.quick.member.common.enums.OrderStatus;
import com.quick.member.dao.PayRecordMapper;
import com.quick.member.domain.po.PayRecordPO;
import com.quick.member.service.PayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付记录表 服务实现类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Service
public class PayRecordServiceImpl extends ServiceImpl<PayRecordMapper, PayRecordPO> implements PayRecordService {

    @Autowired
    private PayRecordMapper payRecordMapper;

    @Override
    public void modifyRecordStatus(String orderNo, OrderStatus status) {
        LambdaQueryWrapper<PayRecordPO> payRecordPOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        payRecordPOLambdaQueryWrapper.eq(PayRecordPO::getOrderNo,orderNo).eq(PayRecordPO::getStatus, Status.VALID);
        PayRecordPO payRecordPO = payRecordMapper.selectOne(payRecordPOLambdaQueryWrapper);
        payRecordPO.setPayStatus(status);
        payRecordMapper.updateById(payRecordPO);
    }
}
