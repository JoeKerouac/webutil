package com.joe.pay.pojo;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * 退款请求
 *
 * @author joe
 * @version 2018.07.05 17:12
 */
@Data
public class RefundRequest {
    /**
     * 第三方账单ID，与outTradeNo不能同时为空
     */
    private String orderId;
    /**
     * 商户系统订单ID，与orderId不能同时为空
     */
    private String outTradeNo;
    /**
     * 商户退款单号
     * <p>
     * 退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。申请退款总金
     * <p>
     * 额不能超过订单金额。 一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号。
     */
    @NotEmpty(message = "商户退款单号不能为空")
    private String outRefundNo;
    /**
     * 订单总金额，单位为分
     */
    @Size(min = 1)
    private int    totalFee;
    /**
     * 退款金额，不能大于订单总金额
     * <p>
     * 注：多次退款总金额不能大于订单总金额
     */
    @Size(min = 1)
    private int    refundFee;
    /**
     * 退款原因
     * <p>
     * 可以为空
     */
    private String refundDesc;
}
