package com.joe.pay.pojo.prop;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.InputStream;

/**
 * 微信支付配置
 *
 * @author joe
 * @version 2018.07.09 10:31
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public final class WxPayProp extends PayProp {
    /**
     * 商户ID
     */
    @NotEmpty(message = "请提供mchId")
    private String mchId;
    /**
     * 证书密码（微信默认证书密码就是mchid）
     */
    private String password;
    /**
     * 微信证书输入流，退款的时候会用到证书，要求PKCS12格式
     * <p>
     * 如果不提供证书则默认不能使用退款功能
     */
    private InputStream certInput;
    /**
     * 商户平台设置的密钥key
     */
    @NotEmpty(message = "请提供商户平台设置的key")
    private String key;

    private WxPayProp() {
        super(PayMode.WECHAT);
    }

    public static final class WxPayPropBuilder extends PayPropBuilder {
        /**
         * 商户ID
         */
        private String mchId;
        /**
         * 证书密码（微信默认证书密码就是mchid）
         */
        private String password;
        /**
         * 微信证书输入流，退款的时候会用到证书，要求PKCS12格式
         * <p>
         * 如果不提供证书则默认不能使用退款功能
         */
        private InputStream certInput;
        /**
         * 商户平台设置的密钥key
         */
        private String key;

        WxPayPropBuilder() {

        }

        public WxPayPropBuilder mchId(String mchId) {
            this.mchId = mchId;
            return this;
        }

        public WxPayPropBuilder password(String password) {
            this.password = password;
            return this;
        }

        public WxPayPropBuilder certInput(InputStream certInput) {
            this.certInput = certInput;
            return this;
        }

        public WxPayPropBuilder key(String key) {
            this.key = key;
            return this;
        }

        public WxPayProp build() {
            WxPayProp prop = new WxPayProp();
            super.build(prop);
            prop.mchId = this.mchId;
            prop.password = this.password;
            prop.certInput = this.certInput;
            prop.key = this.key;
            return prop;
        }

    }
}
