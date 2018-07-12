package com.joe.pay.wechat.pojo;

import com.joe.utils.parse.xml.XmlNode;
import lombok.Data;

/**
 * 支付通知响应（公众号和APP支付）
 *
 * @author joe
 * @version 2018.07.12 16:15
 */
@Data
public class WxPayNotify extends WxPublicResponse {
    /**
     * 用户在商户appid下的唯一标识
     */
    private String openid;
    /**
     * 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
     */
    @XmlNode(name = "is_subscribe")
    private String isSubscribe;
    /**
     * 交易类型，JSAPI、NATIVE、APP
     */
    @XmlNode(name = "trade_type")
    private String tradeType;
    /**
     * 银行类型，采用字符串类型的银行标识
     */
    @XmlNode(name = "bank_type")
    private String bankType;
    /**
     * 订单总金额，单位为分
     */
    @XmlNode(name = "total_fee")
    private Integer totalFee;
    /**
     * 应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     */
    @XmlNode(name = "settlement_total_fee")
    private Integer settlementTotalFee;
    /**
     * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @XmlNode(name = "fee_type")
    private String feeType;
    /**
     * 现金支付金额订单现金支付金额，详见支付金额
     */
    @XmlNode(name = "cash_fee")
    private Integer cashFee;
    /**
     * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @XmlNode(name = "cash_fee_type")
    private String cashFeeType;
    /**
     * 代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
     */
    @XmlNode(name = "coupon_fee")
    private Integer couponFee;
    /**
     * 代金券使用数量
     */
    @XmlNode(name = "coupon_count")
    private Integer couponCount;
    /**
     * 微信支付订单号
     */
    @XmlNode(name = "transaction_id")
    private String transactionId;
    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     */
    @XmlNode(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 商家数据包，原样返回
     */
    private String attach;
    /**
     * 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
     */
    @XmlNode(name = "timeEnd")
    private String time_end;
}
