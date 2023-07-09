package com.quick.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.member.common.enums.OrderStatus;
import com.quick.member.domain.po.OrderPO;

/**
 * <p>
 * 订单记录表 服务类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
public interface OrderService extends IService<OrderPO> {
    /**
     * 根据订单号获取订单信息
     * @param orderNo
     * @return
     */
    OrderPO getOrderByOrderNo(String orderNo);

    /**
     * 根据订单号修改订单状态
     * @param orderNo 订单号
     * @return
     */
    void modifyOrderStatus(String orderNo, OrderStatus status);

    /**
     * 根据订单号修改订单状态以及支付记录
     * @param orderNo 订单号
     * @return
     */
    void modifyOrderAndRecordStatus(String orderNo, OrderStatus status);

    /**
     * 会员到期，取消用户会员
     */
    void cancelMember(Long userId);
}
