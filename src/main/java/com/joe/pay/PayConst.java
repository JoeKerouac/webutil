package com.joe.pay;

/**
 * 支付常量表
 *
 * @author joe
 * @version 2018.07.02 15:45
 */
public class PayConst {
    /**
     * 阿里网关
     */
    public static final String ALI_GATEWAY       = "https://openapi.alipay.com/gateway.do";
    /**
     * 微信网关
     */
    public static final String WX_GATEWAY        = "https://api.mch.weixin.qq.com/pay/";
    /**
     * 阿里下单method
     */
    public static final String ALI_PAY_METHOD    = "alipay.trade.app.pay";
    /**
     * 微信下单method
     */
    public static final String WX_PAY_METHOD     = "unifiedorder";
    /**
     * 阿里退款method
     */
    public static final String ALI_REFUND_METHOD = "alipay.trade.refund";
    /**
     * 微信退款method
     */
    public static final String WX_REFUND_METHOD  = "refund";

}
