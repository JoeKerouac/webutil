package com.joe.pay.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 退款响应
 *
 * @author joe
 * @version 2018.07.05 17:59
 */
@Data
@ToString(callSuper = true)
public class RefundResponse extends BizResponse {
    /**
     * 第三方订单ID（微信/支付宝）
     */
    private String orderId;
    /**
     * 商家订单ID
     */
    private String outTradeNo;
    /**
     * 退款金额，单位为分
     */
    private int    refundFee;
}
