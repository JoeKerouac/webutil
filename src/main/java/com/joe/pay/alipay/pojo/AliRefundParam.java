package com.joe.pay.alipay.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 阿里退款参数
 *
 * @author joe
 * @version 2018.07.05 18:09
 */
@Data
public class AliRefundParam implements BizContent{
    /**
     * 订单支付时传入的商户订单号,不能和 trade_no同时为空。
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 支付宝交易号，和商户订单号不能同时为空
     */
    @JsonProperty("trade_no")
    private String tradeNo;
    /**
     * 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     * <p>
     * 必填，9位
     */
    @NotEmpty(message = "退款金额不能为空")
    @JsonProperty("refund_amount")
    private String refundAmount;
    /**
     * 订单退款币种信息，非外币交易，不能传入退款币种信息
     * <p>
     * 可选，8位
     */
    @JsonProperty("refund_currency")
    private String refundCurrency;
    /**
     * 退款的原因说明
     * <p>
     * 可选，256位
     */
    @JsonProperty("refund_reason")
    private String refundReason;
    /**
     * 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
     * <p>
     * 可选，64位
     */
    @JsonProperty("out_request_no")
    private String outRequestNo;
    /**
     * 商户的操作员编号
     * <p>
     * 可选，30位
     */
    @JsonProperty("operator_id")
    private String operatorId;
    /**
     * 商户的门店编号
     * <p>
     * 可选，32位
     */
    @JsonProperty("store_id")
    private String storeId;
    /**
     * 商户的终端编号
     * <p>
     * 可选，32位
     */
    @JsonProperty("terminal_id")
    private String terminalId;
    /**
     * 退款包含的商品列表信息，Json格式。其它说明详见：“商品明细说明”
     * <p>
     * 可选
     */
    @JsonProperty("goods_detail")
    private String goodsDetail;

    /**
     * 商品信息
     */
    @Data
    public class GoodsDetail {
        /**
         * 商品的编号
         * <p>
         * 必填，最大32位
         */
        @JsonProperty("goodsId")
        private String goods_id;
        /**
         * 支付宝定义的统一商品编号
         * <p>
         * 可选，最大32位
         */
        @JsonProperty("alipayGoodsId")
        private String alipay_goods_id;
        /**
         * 商品名称
         * <p>
         * 可选，最大256位
         */
        @JsonProperty("goodsName")
        private String goods_name;
        /**
         * 商品数量
         * <p>
         * 必填，最大10位
         */
        private int quantity;
        /**
         * 商品单价，单位为元
         * <p>
         * 必填，最大9位
         */
        private String price;
        /**
         * 商品类目
         * <p>
         * 可选，最大24位
         */
        @JsonProperty("goods_category")
        private String goodsCategory;
        /**
         * 商品描述信息
         * <p>
         * 可选，最大1000位
         */
        private String body;
        /**
         * 商品的展示地址
         * <p>
         * 可选，最大400位
         */
        @JsonProperty("show_url")
        private String showUrl;
    }
}
