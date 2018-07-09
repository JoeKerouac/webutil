package com.joe.pay.pojo.prop;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 阿里支付配置
 *
 * @author joe
 * @version 2018.07.09 10:31
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public final class AliPayProp extends PayProp {
    /**
     * 商户平台设置的公钥
     */
    @NotEmpty(message = "请提供publicKey")
    private String publicKey;
    /**
     * 商户平台设置的私钥
     */
    @NotEmpty(message = "请提供privateKey")
    private String privateKey;

    private AliPayProp() {
        super(PayMode.ALIAPP);
    }

    public static final class AliPayPropBuilder extends PayPropBuilder {
        /**
         * 商户平台设置的公钥
         */
        private String publicKey;
        /**
         * 商户平台设置的私钥
         */
        private String privateKey;

        AliPayPropBuilder() {

        }

        public AliPayPropBuilder publicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public AliPayPropBuilder privateKey(String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        public AliPayProp build() {
            AliPayProp prop = new AliPayProp();
            super.build(prop);
            prop.publicKey = this.publicKey;
            prop.privateKey = this.privateKey;
            return prop;
        }
    }

}
