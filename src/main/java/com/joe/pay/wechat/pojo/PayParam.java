package com.joe.pay.wechat.pojo;

import com.joe.utils.parse.xml.XmlNode;
import lombok.Data;

/**
 * 微信支付参数
 *
 * @author joe
 * @version 2018.06.29 11:34
 */
@Data
public class PayParam {
    /**
     * 微信开放平台审核通过的应用APPID
     * <p>
     * 必填
     * <p>
     * 类型：String(32)
     */
    private String appid;
    /**
     * 微信支付分配的商户号
     * <p>
     * 必填
     * <p>
     * 类型：String(32)
     */
    @XmlNode(name = "mch_id")
    private String mchId;
    /**
     * 终端设备号(门店号或收银设备ID)，默认请传"WEB"
     * <p>
     * 非必填
     * <p>
     * 类型：String(32)
     */
    @XmlNode(name = "device_info")
    private String deviceInfo = "WEB";
    /**
     * 随机字符串，不长于32位。推荐随机数生成算法
     * <p>
     * 必填
     * <p>
     * 类型：String(32)
     */
    @XmlNode(name = "nonce_str")
    private String nonceStr;
    /**
     * 签名
     * <p>
     * 必填
     * <p>
     * 类型：String(32)
     */
    private String sign;
    /**
     * 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
     * <p>
     * 非必填
     * <p>
     * 类型：String(32)
     */
    @XmlNode(name = "sign_type")
    private String signType = "MD5";
    /**
     * 商品描述交易字段格式根据不同的应用场景按照以下格式：
     * <p>
     * APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
     * <p>
     * 必填
     * <p>
     * 类型：String(128)
     */
    private String body;
    /**
     * 商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传
     * <p>
     * 非必填
     * <p>
     * 类型：String(8192)
     */
    private String detail;
    /**
     * 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
     * <p>
     * 非必填
     * <p>
     * 类型：String(127)
     */
    private String attach;
    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一
     * <p>
     * 必填
     * <p>
     * 类型：String(32)
     */
    @XmlNode(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 符合ISO 4217标准的三位字母代码，默认人民币：CNY
     * <p>
     * 非必填
     * <p>
     * 类型：String(32)
     */
    @XmlNode(name = "fee_type")
    private String feeType = "CNY";
    /**
     * 订单总金额，单位为分
     * <p>
     * 必填
     * <p>
     * 类型：int
     */
    @XmlNode(name = "total_fee")
    private int totalFee;
    /**
     * 用户端实际ip
     * <p>
     * 必填
     * <p>
     * 类型：String(16)
     */
    @XmlNode(name = "spbill_create_ip")
    private String spbillCreateIp;
    /**
     * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
     * <p>
     * 非必填
     * <p>
     * 类型：String(14)
     */
    @XmlNode(name = "time_start")
    private String timeStart;
    /**
     * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id
     * 只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id
     * <p>
     * 非必填
     * <p>
     * 类型：String(14)
     */
    @XmlNode(name = "time_expire")
    private String timeExpire;
    /**
     * 订单优惠标记，代金券或立减优惠功能的参数
     * <p>
     * 非必填
     * <p>
     * 类型：String(32)
     */
    @XmlNode(name = "goods_tag")
    private String goodsTag;
    /**
     * 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
     * <p>
     * 必填
     * <p>
     * 类型：String(256)
     */
    @XmlNode(name = "notify_url")
    private String notifyUrl;
    /**
     * 支付类型：JSAPI（公众号支付）、NATIVE（扫码支付）、APP（APP支付）
     * <p>
     * 必填
     * <p>
     * 类型：String(16)
     */
    @XmlNode(name = "trade_type")
    private String tradeType = "APP";
    /**
     * no_credit--指定不能使用信用卡支付
     * <p>
     * 非必填
     * <p>
     * 类型：String(32)
     */
    @XmlNode(name = "limit_pay")
    private String limitPay;
    /**
     * 该字段用于统一下单时上报场景信息，目前支持上报实际门店信息。
     * <p>
     * {
     * <p>
     * "store_id": "", //门店唯一标识，选填，String(32)
     * <p>
     * "store_name":"”//门店名称，选填，String(64)
     * <p>
     * }
     * <p>
     * 非必填
     * <p>
     * 类型：String(256)
     */
    @XmlNode(name = "scene_info")
    private String sceneInfo;
    /**
     * 用户openid，只有在公众号支付的时候才有
     */
    private String openid;
}
