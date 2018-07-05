package com.joe.pay.alipay.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 阿里app支付接口2.0响应
 *
 * @author joe
 * @version 2018.06.29 11:18
 */
@Data
public class AliAppPayResponse extends AliPublicResponse {
    /**
     * 商户网站唯一订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 该交易在支付宝系统中的交易流水号。
     */
    @JsonProperty("trade_no")
    private String tradeNo;
    /**
     * 该笔订单的资金总额，单位为RMB-Yuan。取值范围为[0.01，100000000.00]，精确到小数点后两位。
     */
    @JsonProperty("total_amount")
    private String totalAmount;
    /**
     * 收款支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字
     */
    @JsonProperty("seller_id")
    private String sellerId;
}
