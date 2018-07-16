package com.joe.pay.wechat.pojo;

import com.joe.utils.parse.xml.XmlNode;

import lombok.Data;

/**
 * 微信退款响应
 *
 * @author joe
 * @version 2018.07.06 10:03
 */
@Data
public class WxRefundResponse extends WxPublicResponse {
    /**
     * 微信订单号
     * <p>
     * 必填
     */
    @XmlNode(name = "transaction_id")
    private String transactionId;
    /**
     * 商户订单号
     * <p>
     * 必填
     */
    @XmlNode(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 商户退款单号
     * <p>
     * 必填
     */
    @XmlNode(name = "out_refund_no")
    private String outRefundNo;
    /**
     * 微信退款单号
     * <p>
     * 必填
     */
    @XmlNode(name = "refund_id")
    private String refundId;
    /**
     * 退款金额，单位为分
     * <p>
     * 必填
     */
    @XmlNode(name = "refund_fee")
    private int    refundFee;
    /**
     * 应结退款金额，单位为分
     * <p>
     * 选填
     */
    @XmlNode(name = "settlement_refund_fee")
    private int    settlementRefundFee;
    /**
     * 标价金额，单位为分
     * <p>
     * 必填
     */
    @XmlNode(name = "total_fee")
    private int    totalFee;
    /**
     * 应结订单金额，单位为分
     * <p>
     * 选填
     */
    @XmlNode(name = "settlement_total_fee")
    private int    settlementTotalFee;
    /**
     * 标价币种
     * <p>
     * 选填
     */
    @XmlNode(name = "fee_type")
    private String feeType;
    /**
     * 现金支付金额，单位为分
     * <p>
     * 必填
     */
    @XmlNode(name = "cash_fee")
    private int    cashFee;
    /**
     * 现金支付币种
     * <p>
     * 选填
     */
    @XmlNode(name = "cash_fee_type")
    private String cashFeeType;
    /**
     * 现金退款金额，单位为分
     * <p>
     * 选填
     */
    @XmlNode(name = "cash_refund_fee")
    private int    cashRefundFee;
    /**
     * 代金券退款总金额，单位为分
     * <p>
     * 选填
     */
    @XmlNode(name = "coupon_refund_fee")
    private int    couponRefundFee;
    /**
     * 退款代金券使用数量
     * <p>
     * 选填
     */
    @XmlNode(name = "coupon_refund_count")
    private int    couponRefundCount;
}
