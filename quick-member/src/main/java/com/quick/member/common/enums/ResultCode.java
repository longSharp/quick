package com.quick.member.common.enums;

/**
 * @Author: Longcm
 * @Description: 返回码枚举
 * 00开头 基础业务
 * 10开头 短信业务
 * 20开头 会员业务
 * 30开头 gpt业务
 * 40开头 订单业务
 */
public enum ResultCode {
    //################ 基础业务################
    /**
     * 业务请求成功
     */
    REQUEST_SUCCESS("000000","请求成功"),
    /**
     * 业务请求异常
     */
    REQUEST_FAIL("000001","服务器异常"),
    /**
     * 参数校验异常
     */
    PARAMS_VALID_FAIL("000002","参数校验失败"),

    //################短信业务################
    /**
     * 短信发送成功
     */
    SMS_CODE_VALID_SUCCESS("100000","验证码正确"),
    /**
     * 短信发送失败
     */
    SMS_CODE_FAIL("100001","短信发送失败"),
    /**
     * 短信发送频繁
     */
    SMS_CODE_EXCEPTION("100002","60秒内不能再发送"),
    /**
     * 短信发送失败
     */
    SMS_CODE_VALID_FAIL("100003","验证码错误"),

    //################会员业务################
    /**
     * 用户不存在
     */
    USER_NOT_EXISTS("200000","用户不存在"),
    /**
     * 邀请码不存在
     */
    INVIT_CODE_NOT_EXISTS("200001","邀请码不存在"),

    /**
     * 注册失败
     */
    REGISTER_FAIL("200002","注册失败"),

    /**
     * 密码错误
     */
    PASSWORD_ERROR("200003","密码错误"),

    /**
     * 密码修改失败
     */
    PASSWORD_MODIFY_FAIL("200004","修改密码失败"),

    /**
     * sessionId为空
     */
    SESSION_EMPTY("200005","请先登录"),

    /**
     * 登入超时，请重新登入
     */
    LOGIN_TIME_OUT("200006","登入超时，请重新登入"),
    /**
     * 登出失败
     */
    LOGIN_OUT_FAIL("200007","登出失败"),
    /**
     * 用户已签到
     */
    USER_ATTENDANCED("200008","用户已签到"),
    /**
     * 签到失败
     */
    USER_ATTENDANCE_FAIL("200009","签到失败"),
    /**
     * 会员已过期，请重新充值
     */
    MEMBER_EXPIRED("200010","会员已过期，请重新充值"),
    /**
     * 今日会员使用次数已达到上限
     */
    MEMBER_COUNT_COMPLETE("200011","今日会员使用次数已达到上限"),
    /**
     * 查询不到账户,请联系管理员
     */
    NOT_FIND_USE_ACCOUNT("200012","查询不到账户,请联系管理员"),
    /**
     * 使用次数不足，请充值
     */
    USE_ACCOUNT_BALANCE_INSUFFICIENT("200013","使用次数不足，请充值"),
    /**
     * 用户状态异常,请联系管理员
     */
    USE_STATUS_ERROR("200014","用户状态异常,请联系管理员"),
    /**
     * 警告: 问题包含一些敏感词汇，禁止发送色情,暴力,反动等相关词汇
     */
    SENSITIVE_ERROR("200015","警告: 问题包含一些敏感词汇，禁止发送色情,暴力,反动等相关词汇"),
    /**
     * 管理员无需充值
     */
    ADMIN_NOT_PAY("200016","管理员无需充值"),
    /**
     * 已是会员，无需开通
     */
    FOR_MEMBER("200017","已是会员，无需开通"),

    //################gpt业务################
    /**
     * 查询不到账户,请联系管理员
     */
    NETWORK_ERROR("300001","网络繁忙，请稍后再试"),
    /**
     * 内容太长,请减少问题字数
     */
    LENGTH_ERROR("300002","内容太长,请减少问题字数"),
    /**
     * 修改会话失败
     */
    MODIFY_DIALOG_FAIL("300003","修改会话失败"),
    /**
     * 修改会话失败
     */
    ADD_DIALOG_FAIL("300004","新增会话失败"),
    /**
     * 删除会话失败
     */
    REMOVE_DIALOG_FAIL("300005","新增会话失败"),

    //################订单业务################
    /**
     * 卡密不存在
     */
    CARD_SOURCE_NOT_EXISTS("400000","卡密不存在"),
    /**
     * 卡密充值错误
     */
    CARD_SOURCE_ERROR("400001","卡密充值错误"),
    /**
     * 管理员无需兑换
     */
    CARD_ADMIN_ERROR("400002","管理员无需兑换"),
    /**
     * 支付方式错误
     */
    PAYMENT_METHOD_ERROR("400003","支付方式错误"),
    /**
     * 支付失败
     */
    PAYMENT_ERROR("400004","支付失败"),
    /**
     * 商品不存在
     */
    PRODUCT_NOT_EXISTS("400005","产品不存在"),
    /**
     * 商品价格错误
     */
    PRODUCT_PRICE_ERROR("400006","商品价格错误");

    private String code;
    private String msg;
    ResultCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
