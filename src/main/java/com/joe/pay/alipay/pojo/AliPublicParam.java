package com.joe.pay.alipay.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joe.pay.pojo.Request;
import lombok.Data;

import java.nio.charset.Charset;

/**
 * 阿里公共请求参数
 *
 * @author joe
 * @version 2018.06.28 14:42
 */
@Data
public class AliPublicParam implements Request {
    /**
     * 支付宝分配给开发者的应用ID
     * <p>
     * 必填
     * <p>
     * 最大长度：32
     */
    @JsonProperty("app_id")
    private String appId;
    /**
     * 接口名称
     * <p>
     * 必填
     * <p>
     * 最大长度：128
     */
    private String method;
    /**
     * 仅支持JSON
     * <p>
     * 可选
     * <p>
     * 最大长度：40
     */
    private String format;
    /**
     * HTTP/HTTPS开头字符串（支付中没用）
     * <p>
     * 可选
     * <p>
     * 最大长度：256
     */
    @JsonProperty("return_url")
    private String returnUrl;
    /**
     * 请求使用的编码格式，如utf-8,gbk,gb2312等
     * <p>
     * 必填
     * <p>
     * 最大长度：10
     */
    private String charset = Charset.defaultCharset().name().toLowerCase();
    /**
     * 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
     * <p>
     * 必填
     * <p>
     * 最大长度：10
     */
    @JsonProperty("sign_type")
    private String signType = "RSA2";
    /**
     * 商户请求参数的签名串
     * <p>
     * 必填
     * <p>
     * 最大长度：344
     */
    private String sign;
    /**
     * 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
     * <p>
     * 必填
     * <p>
     * 最大长度：19
     */
    private String timestamp;
    /**
     * 调用的接口版本，固定为：1.0
     * <p>
     * 必填
     * <p>
     * 最大长度：3
     */
    private String version = "1.0";
    /**
     * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。
     * <p>
     * 可选
     * <p>
     * 最大长度：256
     */
    @JsonProperty("notify_url")
    private String notifyUrl;
    /**
     * 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
     * <p>
     * 必填
     */
    @JsonProperty("biz_content")
    private String bizContent;
}
