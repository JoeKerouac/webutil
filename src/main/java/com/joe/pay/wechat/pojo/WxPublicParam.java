package com.joe.pay.wechat.pojo;

import com.joe.pay.pojo.Request;
import com.joe.utils.serialize.xml.XmlNode;

import lombok.Data;

/**
 * 微信公共参数
 *
 * @author joe
 * @version 2018.07.02 10:52
 */
@Data
public class WxPublicParam implements Request {
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
     * 签名（采用MD5签名）
     * <p>
     * 必填
     * <p>
     * 类型：String(32)
     */
    private String sign;
    /**
     * 随机字符串，不长于32位。推荐随机数生成算法
     * <p>
     * 必填
     * <p>
     * 类型：String(32)
     */
    @XmlNode(name = "nonce_str")
    private String nonceStr;
}
