package com.joe.pay.alipay.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 阿里退款响应
 *
 * @author joe
 * @version 2018.07.05 18:45
 */
@Data
@ToString(callSuper = true)
public class AliRefundResponse extends AliPublicResponse {
    /**
     * 订单支付时传入的商户订单号,不能和 trade_no同时为空。
     * <p>
     * 必填，最大长度64
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 支付宝交易号，和商户订单号不能同时为空
     * <p>
     * 必填，最大长度64
     */
    @JsonProperty("trade_no")
    private String tradeNo;
    /**
     * 用户的登录id
     * <p>
     * 必填，最大长度100
     */
    @JsonProperty("buyer_logon_id")
    private String buyerLogonId;
    /**
     * 本次退款是否发生了资金变化
     * <p>
     * 必填，最大长度1
     */
    @JsonProperty("fund_change")
    private String fundChange;
    /**
     * 退款总金额
     * <p>
     * 必填，最大长度1
     */
    @JsonProperty("refund_fee")
    private Double refundFee;
    /**
     * 错误时有，暂时不知道有什么用
     */
    @JsonProperty("send_back_fee")
    private Double sendBackFee;
    /**
     * 退款币种信息
     * <p>
     * 必填，最大长度8
     */
    @JsonProperty("refund_currency")
    private String refundCurrency;
    /**
     * 退款支付时间
     * <p>
     * 必填，最大长度32
     */
    @JsonProperty("gmt_refund_pay")
    private String gmtRefundPay;
    /**
     * 退款使用的资金渠道
     * <p>
     * 选填
     */
    @JsonProperty("refund_detail_item_list")
    private TradeFundBill refundDetailItemList;
    /**
     * 交易在支付时候的门店名称
     * <p>
     * 选填，最大长度512
     */
    @JsonProperty("store_name")
    private String storeName;
    /**
     * 买家在支付宝的用户id
     * <p>
     * 选填，最大长度28
     */
    @JsonProperty("buyer_user_id")
    private String buyerUserId;
    /**
     * 本次退款金额中买家退款金额
     * <p>
     * 选填，最大长度11
     */
    @JsonProperty("present_refund_buyer_amount")
    private String presentRefundBuyerAmount;
    /**
     * 本次退款金额中平台优惠退款金额
     * <p>
     * 选填，最大长度11
     */
    @JsonProperty("present_refund_discount_amount")
    private String presentRefundDiscountAmount;
    /**
     * 本次退款金额中商家优惠退款金额
     * <p>
     * 选填，最大长度11
     */
    @JsonProperty("present_refund_mdiscount_amount")
    private String presentRefundMdiscountAmount;

    @Data
    public class TradeFundBill {
        /**
         * 交易使用的资金渠道
         *
         * 必填，最大长度32
         */
        @JsonProperty("fund_channel")
        private String fundChannel;
        /**
         * 银行卡支付时的银行代码
         *
         * 可选，最大长度10
         */
        @JsonProperty("bank_code")
        private String bankCode;
        /**
         * 该支付工具类型所使用的金额
         *
         * 必填，最大长度32
         */
        private String amount;
        /**
         * 渠道实际付款金额
         *
         * 可选，最大长度11
         */
        @JsonProperty("real_amount")
        private String realAmount;
        /**
         * 渠道所使用的资金类型,目前只在资金渠道(fund_channel)是银行卡渠道(BANKCARD)的情况下才返回该信息(DEBIT_CARD:借记卡,CREDIT_CARD:信用卡,MIXED_CARD:借贷合一卡)
         *
         * 可选，最大长度32
         */
        @JsonProperty("fund_type")
        private String fundType;
    }

}
