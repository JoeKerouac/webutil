package com.joe.pay.pojo.prop;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 支付配置
 *
 * @author joe
 * @version 2018.07.02 14:31
 */
@Getter
@EqualsAndHashCode
public class PayProp {
    /**
     * 支付模式
     */
    @NotNull(message = "支付模式不能为空")
    private PayMode     mode;
    /**
     * appid
     */
    @NotEmpty(message = "请提供appid")
    private String      appid;
    /**
     * 支付异步回调通知地址
     */
    @NotEmpty(message = "请提供notifyUrl")
    private String      notifyUrl;
    /**
     * 环境信息，为空时默认使用PROD
     */
    private Environment environment = Environment.PROD;

    PayProp(PayMode mode) {
        this.mode = mode;
    }

    public static PayPropBuilder builder() {
        return new PayPropBuilder();
    }

    public static class PayPropBuilder {
        /**
         * appid
         */
        @NotEmpty(message = "appid不能为空")
        private String      appid;
        /**
         * 支付异步回调通知地址
         */
        @NotEmpty(message = "notifyUrl不能为空")
        private String      notifyUrl;
        /**
         * 环境信息，为空时默认使用PROD
         */
        private Environment environment = Environment.PROD;

        PayPropBuilder() {

        }

        /**
         * 使用阿里支付
         *
         * @return 阿里支付配置构建器
         */
        public AliPayProp.AliPayPropBuilder useAliPay() {
            return merge(new AliPayProp.AliPayPropBuilder());
        }

        /**
         * 使用微信支付
         *
         * @return 微信支付配置构建器
         */
        public WxPayProp.WxPayPropBuilder useWxPay() {
            return merge(new WxPayProp.WxPayPropBuilder());
        }

        /**
         * 将本配置和指定配置合并
         *
         * @param prop 指定合并的配置
         * @param <T>  配置类型
         * @return 合并后的配置
         */
        private <T extends PayPropBuilder> T merge(T prop) {
            prop.appid(this.appid);
            prop.notifyUrl(this.notifyUrl);
            prop.environment(this.environment);
            return prop;
        }

        public PayPropBuilder appid(String appid) {
            this.appid = appid;
            return this;
        }

        public PayPropBuilder notifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
            return this;
        }

        public PayPropBuilder environment(Environment environment) {
            this.environment = environment == null ? Environment.PROD : environment;
            return this;
        }

        protected PayProp build(PayProp prop) {
            prop.appid = this.appid;
            prop.notifyUrl = this.notifyUrl;
            prop.environment = this.environment;
            return prop;
        }
    }

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
