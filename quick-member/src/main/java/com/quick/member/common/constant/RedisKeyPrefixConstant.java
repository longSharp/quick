package com.quick.member.common.constant;

/**
 * @Author: Longcm
 * @Description: redis键前缀
 */
public class RedisKeyPrefixConstant {
    /**
     * 产品购买
     */
    public static final String WECHAT_PRODUCT_PREFIX = "wechat:product:";

    /**
     * 订单延迟队列名称
     */
    public static final  String  DELAY_QUEUE_NAME="order_delay_queue";

    /**
     * 会员(日)延迟队列名称
     */
    public static final  String  MEMBER_DAY_QUEUE_NAME="member_day_delay_queue";
    /**
     * 会员(周)延迟队列名称
     */
    public static final  String  MEMBER_WEEK_QUEUE_NAME="member_week_delay_queue";
    /**
     * 会员(月)延迟队列名称
     */
    public static final  String  MEMBER_MONTH_QUEUE_NAME="member_month_delay_queue";
    /**
     * 会员(季)延迟队列名称
     */
    public static final  String  MEMBER_QUARTER_QUEUE_NAME="member_quarter_delay_queue";
    /**
     * 会员(年)延迟队列名称
     */
    public static final  String  MEMBER_YEAR_QUEUE_NAME="member_year_delay_queue";

    /**
     * 分布式锁前缀(订单)
     */
    public static final String LOCAK_ORDER_PREFIX = "order:";

    /**
     * 分布式锁前缀(会员)
     */
    public static final String LOCAK_MEMBER_PREFIX = "member:";
}
