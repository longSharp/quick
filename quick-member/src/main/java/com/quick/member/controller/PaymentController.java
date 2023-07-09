package com.quick.member.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.quick.member.common.config.exception.BusinessException;
import com.quick.member.common.config.params.WechatPayParamsConfig;
import com.quick.member.common.constant.RedisKeyPrefixConstant;
import com.quick.member.common.enums.OrderStatus;
import com.quick.member.common.enums.ResultCode;
import com.quick.member.common.enums.WechatPayMentStatus;
import com.quick.member.domain.dto.req.*;
import com.quick.member.domain.dto.resp.OrderStatusRespDTO;
import com.quick.member.domain.dto.resp.R;
import com.quick.member.domain.dto.resp.WechatNotifyRespDTO;
import com.quick.member.domain.po.OrderPO;
import com.quick.member.service.IMessageDelayQueueService;
import com.quick.member.service.OrderService;
import com.quick.member.service.WechatPaymentService;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private WechatPayParamsConfig wechatPayParamsConfig;

    @Autowired
    private WechatPaymentService wechatPaymentService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private OrderService orderService;

    @Autowired
    private IMessageDelayQueueService delayQueueService;


    @PostMapping("/createOrder")
    public R<WechatPaymentDTO> createOrder(@Valid @NotNull @RequestBody OrderInfoReqDTO orderDto, HttpServletRequest request){
        String sessionId = request.getHeader("sessionId");
        String[] userIds = sessionId.split("-");
        OrderInfo orderInfo = BeanUtil.copyProperties(orderDto,OrderInfo.class);
        orderInfo.setUserId(Long.parseLong(userIds[1]));
        WechatPaymentDTO payment = wechatPaymentService.createWechatOrderAll(orderInfo);
        return R.ok(payment);
    }

    @PostMapping("/recharge/{orderNo}/{status}")
    public R recharge(@NotBlank @PathVariable String orderNo,@NotBlank @PathVariable Integer status){
        orderService.modifyOrderAndRecordStatus(orderNo,OrderStatus.valueOf(status));
        //9.将订单从redis缓存移除
        RedisDelayMessage message = new RedisDelayMessage();
        message.setOrderNo(orderNo);
        delayQueueService.remove(message,RedisKeyPrefixConstant.DELAY_QUEUE_NAME);
        return R.ok();
    }

    @GetMapping("/getOrderStatus/{orderNo}")
    public R<OrderStatusRespDTO> getOrderStatus(@NotBlank @PathVariable String orderNo){
        WechatPayMentStatus wechatPayMentStatus = wechatPaymentService.queryOrderForWechat(orderNo);
        OrderStatusRespDTO orderStatusRespDTO = new OrderStatusRespDTO();
        orderStatusRespDTO.setStatus(wechatPayMentStatus);
        return R.ok(orderStatusRespDTO);
    }

    @PostMapping("/notify")
    public WechatNotifyRespDTO notify(@RequestBody Map<String, Object> signalRes){
        WechatNotifyRespDTO resp = new WechatNotifyRespDTO();
        RLock lock = null;
        try {
            //用密文解密出明文
            Map<String,String> resource=(Map<String,String>)signalRes.get("resource");
            String ciphertext=resource.get("ciphertext");
            String associatedData=resource.get("associated_data");
            String nonce=resource.get("nonce");
            String plainText=new AesUtil(wechatPayParamsConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8)).decryptToString(associatedData.getBytes(StandardCharsets.UTF_8),nonce.getBytes(StandardCharsets.UTF_8),ciphertext);
            WechatNotifyResourceReqDTO resourceReqDTO = JSONUtil.toBean(plainText, WechatNotifyResourceReqDTO.class);
            log.info("支付回调：orderNo={},time={}",resourceReqDTO.getOut_trade_no(),resourceReqDTO.getSuccess_time());
            //1.订单是否支付成功
            if(!("SUCCESS".equals(resourceReqDTO.getTrade_state()))) {
                throw new BusinessException(ResultCode.REQUEST_FAIL,"order {"+resourceReqDTO.getOut_trade_no()+"} payment status is not success or get lock fail");
            }
            //加分布式锁
            lock = redissonClient.getLock(RedisKeyPrefixConstant.LOCAK_ORDER_PREFIX+resourceReqDTO.getOut_trade_no());
            boolean isLock = lock.tryLock();
            if(isLock){
                OrderPO orderByOrderNo = orderService.getOrderByOrderNo(resourceReqDTO.getOut_trade_no());
                if(orderByOrderNo.getOrderStatus()== OrderStatus.WAIT_PAY){
                    WechatPayMentStatus wechatPayMentStatus = wechatPaymentService.queryOrderForWechat(resourceReqDTO.getOut_trade_no());
                    if(wechatPayMentStatus==null) {
                        resp.setCode("FAIL");
                        resp.setMessage("失败-支付回调失败");
                        return resp;
                    }
                    switch (wechatPayMentStatus){
                        case SUCCESS:
                            orderService.modifyOrderAndRecordStatus(resourceReqDTO.getOut_trade_no(),OrderStatus.PAID);
                            break;
                        case NOTPAY:
                            wechatPaymentService.closeOrder(resourceReqDTO.getOut_trade_no());
                            break;
                    }
                    RedisDelayMessage message = new RedisDelayMessage();
                    message.setOrderNo(resourceReqDTO.getOut_trade_no());
                    delayQueueService.remove(message,RedisKeyPrefixConstant.DELAY_QUEUE_NAME);
                    resp.setCode("SUCCESS");
                    resp.setMessage("成功");
                    return resp;
                }
                if(orderByOrderNo.getOrderStatus()== OrderStatus.UNPAID||orderByOrderNo.getOrderStatus()== OrderStatus.PAID){
                    resp.setCode("SUCCESS");
                    resp.setMessage("成功");
                    return resp;
                }

                resp.setCode("FAIL");
                resp.setMessage("失败-支付回调失败");
                return resp;
            }
            //转换
            resp.setCode("FAIL");
            resp.setMessage("失败-支付回调失败");
            return resp;

        } catch (Exception e) {
            e.printStackTrace();
            //失败应答
            resp.setCode("FAIL");
            resp.setMessage("失败-支付回调失败");
            return resp;
        }finally {
            if(lock!=null){
                lock.unlock();
            }
        }
    }


}
