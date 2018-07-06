package com.joe.pay.pojo;

import lombok.Data;

/**
 * 支付结果
 *
 * @author joe
 * @version 2018.07.02 11:47
 */
@Data
public class PayResponse extends BizResponse {
    /**
     * 结果，微信中该字段对应prepayId，支付宝中该字段对应订单信息
     */
    private String info;
}
