package com.quick.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.member.common.enums.OrderStatus;
import com.quick.member.domain.po.PayRecordPO;

/**
 * <p>
 * 支付记录表 服务类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
public interface PayRecordService extends IService<PayRecordPO> {
    /**
     * 根据订单号修改支付记录状态
     * @param orderNo
     * @param status
     */
    void modifyRecordStatus(String orderNo, OrderStatus status);
}
