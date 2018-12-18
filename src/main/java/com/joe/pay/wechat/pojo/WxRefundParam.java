package com.joe.pay.wechat.pojo;

import com.joe.utils.serialize.xml.XmlNode;

import lombok.Data;

/**
 * 微信退款请求
 *
 * @author joe
 * @version 2018.07.06 15:47
 */
@Data
public class WxRefundParam extends WxPublicParam {
    /**
     * 微信订单号，与outTradeNo不能同时为空
     * <p>
     * 最大32
     */
    @XmlNode(name = "transactionId")
    private String transaction_id;
    /**
     * 商户订单号，与transaction_id不能同时为空
     * <p>
     * 最大32
     */
    @XmlNode(name = "outTradeNo")
    private String outTradeNo;
    /**
     * 商户退款单号
     * <p>
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     * <p>
     * 不可为空，最大64
     */
    @XmlNode(name = "out_refund_no")
    private String outRefundNo;
    /**
     * 订单金额，单位为分
     */
    @XmlNode(name = "total_fee")
    private int    totalFee;
    /**
     * 退款金额，单位为分
     */
    @XmlNode(name = "refund_fee")
    private int    refundFee;
    /**
     * 退款货币种类
     * <p>
     * 可为空，最大8
     */
    @XmlNode(name = "refund_fee_type")
    private String refundFeeType;
    /**
     * 退款原因
     * <p>
     * 可为空，最大80
     */
    @XmlNode(name = "refund_desc")
    private String refundDesc;
    /**
     * 退款资金来源
     * <p>
     * 可为空，最大30
     */
    @XmlNode(name = "refund_account")
    private String refundAccount;
    /**
     * 退款结果通知url
     * <p>
     * 可为空，最大256
     */
    @XmlNode(name = "refund_account")
    private String notifyUrl;
}
