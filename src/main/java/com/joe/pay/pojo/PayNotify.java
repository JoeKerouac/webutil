package com.joe.pay.pojo;

import com.joe.utils.parse.xml.XmlNode;

import lombok.Data;

/**
 * 支付通知结果
 *
 * @author joe
 * @version 2018.07.12 16:46
 */
@Data
public class PayNotify extends BizResponse {
    /**
     * 商户订单号
     */
    private String  outTradeNo;
    /**
     * 第三方订单号
     */
    private String  orderId;
    /**
     * 商家数据包，原样返回
     */
    private String  attach;
    /**
     * 支付类型：APP、WEB（微信对应公众号，支付宝对应网页支付）
     */
    private String  tradeType = "APP";
    /**
     * 订单总金额，单位为分
     */
    @XmlNode(name = "total_fee")
    private Integer totalFee;
    /**
     * 现金支付金额
     */
    @XmlNode(name = "cash_fee")
    private Integer cashFee;
}
