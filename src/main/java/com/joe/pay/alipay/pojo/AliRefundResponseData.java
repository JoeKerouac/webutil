package com.joe.pay.alipay.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 阿里退款响应（原响应）
 *
 * @author joe
 * @version 2018.07.06 18:10
 */
public class AliRefundResponseData implements AliResponseData<AliRefundResponse> {
    @JsonProperty("alipay_trade_refund_response")
    public AliRefundResponse alipayTradeRefundResponse;
    public String sign;

    @Override
    public AliRefundResponse getData() {
        return alipayTradeRefundResponse;
    }

    @Override
    public String getSign() {
        return sign;
    }
}
