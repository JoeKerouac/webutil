package com.joe.pay.pojo;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 支付配置
 *
 * @author joe
 * @version 2018.07.02 14:31
 */
@Data
public class PayProp {
    /**
     * appid
     */
    @NotEmpty(message = "appid不能为空")
    private String appid;
    /**
     * 商户ID（微信支付有）
     */
    private String mchId;
    /**
     * 商户平台设置的密钥key
     */
    @NotEmpty(message = "key不能为空")
    private String key;
    /**
     * 支付异步回调通知地址
     */
    @NotEmpty(message = "notifyUrl不能为空")
    private String notifyUrl;
}
