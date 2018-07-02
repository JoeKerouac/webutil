package com.joe.pay.wechat.pojo;

import com.joe.utils.parse.xml.XmlNode;
import lombok.Data;
import lombok.ToString;

/**
 * 微信支付响应
 *
 * @author joe
 * @version 2018.06.29 17:53
 */
@Data
@ToString(callSuper = true)
public class WxPayResponse extends WxPublicResponse {
    /*-----------------以下字段在return_code 和result_code都为SUCCESS的时候有返回-----------------*/
    /**
     * 交易类型:调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，
     */
    @XmlNode(name = "trade_type")
    private String tradeType;
    /**
     * 预支付交易会话标识
     */
    @XmlNode(name = "prepay_id")
    private String prepayId;
}
