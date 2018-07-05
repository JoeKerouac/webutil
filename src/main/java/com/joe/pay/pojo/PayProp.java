package com.joe.pay.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 支付配置
 *
 * @author joe
 * @version 2018.07.02 14:31
 */
@Data
@EqualsAndHashCode
public final class PayProp {
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
    /**
     * 支付模式
     */
    private PayMode mode;
    /**
     * 环境信息
     */
    private Environment environment;

    /**
     * 支付模式（后缀SANDBOX的为沙箱模式）
     */
    public enum PayMode {
        ALIAPP, WECHAT
    }

    /**
     * 环境信息，PROD表示生产，SANDBOX表示沙箱，如果对应的支付服务支持沙箱模式那么会进入沙箱模式
     */
    public enum Environment {
        PROD, SANDBOX
    }
}
