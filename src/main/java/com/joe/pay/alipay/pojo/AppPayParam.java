package com.joe.pay.alipay.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * app支付请求业务参数
 *
 * @author joe
 * @version 2018.06.28 14:40
 */
@Data
public class AppPayParam implements BizContent {
    /**
     * 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
     * <p>
     * 可选
     * <p>
     * 最大长度：128
     */
    private String body;
    /**
     * 商品的标题/交易标题/订单标题/订单关键字等。
     * <p>
     * 必填
     * <p>
     * 最大长度：256
     */
    private String subject;
    /**
     * 商户网站唯一订单号
     * <p>
     * 必填
     * <p>
     * 最大长度：64
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
     * <p>
     * 可选
     * <p>
     * 最大长度：6
     */
    @JsonProperty("timeout_express")
    private String timeoutExpress;
    /**
     * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
     * <p>
     * 必填
     * <p>
     * 最大长度：9
     */
    @JsonProperty("total_amount")
    private String totalAmount;
    /**
     * 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
     * <p>
     * 必填
     * <p>
     * 最大长度：64
     */
    @JsonProperty("product_code")
    private String productCode = "QUICK_MSECURITY_PAY";
    /**
     * 商品主类型 :0-虚拟类商品,1-实物类商品
     * <p>
     * 可选
     * <p>
     * 最大长度：2
     */
    @JsonProperty("goods_type")
    private String goodsType;
    /**
     * 公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数。支付宝只会在同步返回（包括跳转回商户网站）和异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝。
     * <p>
     * 可选
     * <p>
     * 最大长度：512
     */
    @JsonProperty("passback_params")
    private String passbackParams;
    /**
     * 优惠参数
     * <p>
     * 注：仅与支付宝协商后可用
     * <p>
     * 可选
     * <p>
     * 最大长度：512
     */
    @JsonProperty("promo_params")
    private String promoParams;
    /**
     * 业务扩展参数，字段参见{@link ExtendParams ExtendParams}
     * <p>
     * 可选
     */
    @JsonProperty("extend_params")
    private String extendParams;
    /**
     * 可用渠道，用户只能在指定渠道范围内支付，当有多个渠道时用“,”分隔
     * <p>
     * 注，与disable_pay_channels互斥
     * <p>
     * 可选
     * <p>
     * 最大长度：128
     */
    @JsonProperty("enable_pay_channels")
    private String enablePayChannels;
    /**
     * 禁用渠道，用户不可用指定渠道支付，当有多个渠道时用“,”分隔
     * <p>
     * 注，与enable_pay_channels互斥
     * <p>
     * 可选
     * <p>
     * 最大长度：128
     */
    @JsonProperty("disable_pay_channels")
    private String disablePayChannels;
    /**
     * 商户门店编号
     * <p>
     * 可选
     * <p>
     * 最大长度：32
     */
    @JsonProperty("store_id")
    private String storeId;

    /**
     * 扩展参数
     */
    @Data
    static class ExtendParams {
        /**
         * 系统商编号
         * <p>
         * 该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID
         * <p>
         * 可选
         * <p>
         * 最大长度：64
         */
        @JsonProperty("sys_service_provider_id")
        private String sysServiceProviderId;
        /**
         * 是否发起实名校验
         * <p>
         * T：发起
         * <p>
         * F：不发起
         * <p>
         * 可选
         * <p>
         * 最大长度：1
         */
        private String needBuyerRealnamed;
        /**
         * 账务备注
         * <p>
         * 注：该字段显示在离线账单的账务备注中
         * <p>
         * 可选
         * <p>
         * 最大长度：128
         */
        @JsonProperty("TRANS_MEMO")
        private String transMemo;
        /**
         * 使用花呗分期要进行的分期数
         * <p>
         * 可选
         * <p>
         * 最大长度：5
         */
        @JsonProperty("hbFqNum")
        private String hb_fq_num;
        /**
         * 使用花呗分期需要卖家承担的手续费比例的百分值，传入100代表100%
         * <p>
         * 可选
         * <p>
         * 最大长度：3
         */
        @JsonProperty("hb_fq_seller_percent")
        private String hbFqSellerPercent;

    }
}
