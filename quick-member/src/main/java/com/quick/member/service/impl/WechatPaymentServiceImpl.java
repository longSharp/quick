package com.quick.member.service.impl;

import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.quick.common.enums.ResultCode;
import com.quick.common.exception.BusinessException;
import com.quick.member.common.config.params.WechatPayParamsConfig;
import com.quick.member.common.config.redis.MessageProducer;
import com.quick.member.common.enums.*;
import com.quick.member.common.utils.OrderNoUtils;
import com.quick.member.domain.dto.req.RedisDelayMessage;
import com.quick.member.domain.dto.req.OrderInfo;
import com.quick.member.domain.dto.req.WechatPaymentDTO;
import com.quick.member.domain.dto.resp.SysUserInfoRespDTO;
import com.quick.member.domain.po.OrderPO;
import com.quick.member.domain.po.PayRecordPO;
import com.quick.member.domain.po.ProductPO;
import com.quick.member.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WechatPaymentServiceImpl implements WechatPaymentService {

    @Autowired
    private WechatPayParamsConfig wechatPayParamsConfig;

    @Autowired
    private MessageProducer productor;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayRecordService payRecordService;

    @Override
    public WechatPaymentDTO createWechatOrderAll(OrderInfo orderInfo) {
        //1. 校验用户
        checkUser(orderInfo.getUserId(),orderInfo.getAsCard());
        //2. 校验产品，校验价格
        ProductPO product = productService.getById(orderInfo.getProductId());
        checkProduct(product);
        //3. 本地创建订单，订单状态为创建订单
        OrderPO orderPO = new OrderPO();
        String orderNo = OrderNoUtils.getNo();

        //3.1 查询优惠券


        orderPO.setOrderNo(orderNo)
                .setOrderStatus(OrderStatus.CREATED)
                .setMoney(product.getSalePrice())
                .setUserId(orderInfo.getUserId())
                .setProductId(orderInfo.getProductId())
                .setProductType(product.getType())
                .setOriginalPrice(product.getOriginalPrice())
                .setUseCount(product.getUseCount());
        orderService.save(orderPO);
        // 4.构建统一下单请求参数对象
        WxPayUnifiedOrderV3Request wxPayUnifiedOrderV3Request = new WxPayUnifiedOrderV3Request();
        WechatPaymentDTO resp = new WechatPaymentDTO();
        resp.setOrderNo(orderNo);
        WxPayService wxPayService = this.getWxPayService();
        Map<String,String> attach = new HashMap<>();
        // 5.对象中写入数据
        wxPayUnifiedOrderV3Request
                // 【1】必填信息
                // 商品描述：必填
                .setDescription(orderInfo.getTitle())
                // 商户订单号：必填，同一个商户号下唯一
                .setOutTradeNo(orderNo)
                // 通知地址：必填，公网域名必须为https，外网可访问。可不填，通过配置信息读取
                .setNotifyUrl(wechatPayParamsConfig.getNotifyDomain())
                // 订单金额：单位（分）
                .setAmount(new WxPayUnifiedOrderV3Request.Amount().setTotal(product.getSalePrice().intValue()))
                .setAppid(wechatPayParamsConfig.getAppId())
                .setAttach(JSONUtil.toJsonStr(attach));
        try {
            //6.微信预下单
            switch (orderInfo.getTradeType()) {
                // Native支付
                case NATIVE:
                    resp.setCodeUrl(wxPayService.unifiedOrderV3(TradeTypeEnum.NATIVE, wxPayUnifiedOrderV3Request).getCodeUrl());
                    break;
                // JSAPI支付
                case JSAPI:
                    // 用户在直连商户appid下的唯一标识。 下单前需获取到用户的Openid
                    wxPayUnifiedOrderV3Request.setPayer(new WxPayUnifiedOrderV3Request.Payer().setOpenid(orderInfo.getOpenid()));
                    resp.setPrepayId(wxPayService.unifiedOrderV3(TradeTypeEnum.JSAPI, wxPayUnifiedOrderV3Request).getPrepayId());
                    break;
                // H5支付
                case H5:
                    wxPayUnifiedOrderV3Request.setSceneInfo(
                            new WxPayUnifiedOrderV3Request.SceneInfo()
                                    // 用户终端IP
                                    .setPayerClientIp("12.34.56.78")
                                    .setH5Info(
                                            new WxPayUnifiedOrderV3Request.H5Info()
                                                    // 场景类型
                                                    .setType("wechat")
                                    )
                    );
                    resp.setH5Url(wxPayService.unifiedOrderV3(TradeTypeEnum.H5, wxPayUnifiedOrderV3Request).getH5Url());
                    break;
                // APP支付
                case APP:
                    resp.setPrepayId(wxPayService.unifiedOrderV3(TradeTypeEnum.APP, wxPayUnifiedOrderV3Request).getPrepayId());
                    break;
                default:
                    throw new BusinessException(ResultCode.PAYMENT_METHOD_ERROR);
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.PAYMENT_ERROR);
        }
        //7. 微信方预订单创建成功，更新本地订单为待支付
        orderPO.setOrderStatus(OrderStatus.WAIT_PAY);
        orderService.updateById(orderPO);
        //8.生成支付记录
        PayRecordPO payRecordPO = new PayRecordPO();
        //判断操作类型
        OperationType opType = orderInfo.getAsCard()? OperationType.CARD_RECHARGE:(product.getType()==ProductType.COUNT_CARD?OperationType.COUNT_RECHARGE:OperationType.MEMBER_RECHARGE);
        payRecordPO.setOrderNo(orderNo)
                .setUserId(orderInfo.getUserId())
                .setOperationType(opType)
                .setMoney(product.getSalePrice())
                .setPayStatus(OrderStatus.WAIT_PAY);
        payRecordService.save(payRecordPO);
        //9.将订单存到redis缓存
        RedisDelayMessage message = new RedisDelayMessage();
        message.setOrderNo(orderNo);
        productor.pushMessage(message);
        return resp;
    }

    @Override
    public WechatPayMentStatus queryOrderForWechat(String orderNo) {
        // 执行查询并返回查询结果
        WxPayOrderQueryV3Result wxPayOrderQueryV3Result;
        try {
            wxPayOrderQueryV3Result = this.getWxPayService().queryOrderV3(null, orderNo);
        } catch (WxPayException e) {
            e.printStackTrace();
            return null;
        }
        if ("SUCCESS".equals(wxPayOrderQueryV3Result.getTradeState())){
            return WechatPayMentStatus.SUCCESS;
        }
        if ("NOTPAY".equals(wxPayOrderQueryV3Result.getTradeState())){
            return WechatPayMentStatus.NOTPAY;
        }
        return WechatPayMentStatus.CLOSED;
    }

    @Override
    public void closeOrder(String orderNo) {
        try {
            this.getWxPayService().closeOrder(orderNo);
            orderService.modifyOrderAndRecordStatus(orderNo,OrderStatus.UNPAID);
            log.info("订单{}超时自动取消!",orderNo);
        } catch (WxPayException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取微信支付相关接口服务（后续的几个服务方法，实现了基本的实例）
     * （此接口也可以直接在controller中使用）
     *
     * @return 微信支付服务接口
     */
    public WxPayService getWxPayService(){
        // 生成配置
        WxPayConfig payConfig = new WxPayConfig();
        // 填充基本配置信息
        payConfig.setAppId(StringUtils.trimToNull(wechatPayParamsConfig.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(wechatPayParamsConfig.getMerchantId()));
        payConfig.setMchKey(StringUtils.trimToNull(wechatPayParamsConfig.getMchKey()));
        payConfig.setApiV3Key(StringUtils.trimToNull(wechatPayParamsConfig.getApiV3Key()));
        payConfig.setKeyPath(StringUtils.trimToNull(wechatPayParamsConfig.getKeyPath()));
        payConfig.setPrivateCertPath(StringUtils.trimToNull(wechatPayParamsConfig.getPrivateCertPath()));
        payConfig.setPrivateKeyPath(StringUtils.trimToNull(wechatPayParamsConfig.getPrivateKeyPath()));
        payConfig.setNotifyUrl(StringUtils.trimToNull(wechatPayParamsConfig.getNotifyDomain()));
        payConfig.setUseSandboxEnv(false);
        // 创建配置服务
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        // 返回结果
        return wxPayService;
    }

    private void checkUser(Long userId,Boolean asCard){
        SysUserInfoRespDTO sysUserInfoRespDTO = sysUserService.queryUserById(userId);
        UserStatus userStatus = sysUserInfoRespDTO.getUserStatus();
        if(!UserStatus.NORMAL.equals(userStatus)){
            throw new BusinessException(ResultCode.USE_STATUS_ERROR);
        }
        if(UserRole.TOURIST.equals(sysUserInfoRespDTO.getRole())){
            throw new BusinessException(ResultCode.TOURIST_NOT_PAY);
        }
        if(MemberMark.MEMBER.equals(sysUserInfoRespDTO.getIsMember())&&!asCard){
            throw new BusinessException(ResultCode.FOR_MEMBER);
        }

    }

    private void checkProduct(ProductPO product){
        if(product==null){
            throw new BusinessException(ResultCode.PRODUCT_NOT_EXISTS);
        }
    }
}
