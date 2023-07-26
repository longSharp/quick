package com.quick.common.constant;

/**
 * @Author: Longcm
 * @Description: redis键前缀
 */
public class RedisKeyPrefixConstant {
    /**
     * 生成图片任务
     */
    public static final String IMAGE_TASK = "image_task:";

    /**
     * 已完成生成图片任务
     */
    public static final String SUCCESS_IMAGE_TASK = "success_image_task:";

    /**
     * 临时记录task所属者
     */
    public static final String TASK_USER_ID = "task_user_id:";

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
