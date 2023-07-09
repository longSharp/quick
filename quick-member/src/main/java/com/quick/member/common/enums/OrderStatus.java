package com.quick.member.common.enums;

public enum OrderStatus implements IBaseEnum{
    /**
     * 创建订单
     */
    CREATED(0,"创建订单"),
    /**
     * 待支付
     */
    WAIT_PAY(1,"待支付"),
    /**
     * 支付失败
     */
    UNPAID(2,"支付失败"),
    /**
     * 已支付
     */
    PAID(3,"已支付");

    private Integer code;
    private String name;

    OrderStatus(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static OrderStatus valueOf(int ordinal) {
        OrderStatus[] values = OrderStatus.values();
        if (ordinal >= values.length) {
            return null;
        }
        else {
            return values[ordinal];
        }
    }
}
