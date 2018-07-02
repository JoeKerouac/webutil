package com.joe.pay.wechat.service;

import com.joe.http.IHttpClientUtil;
import com.joe.pay.PayService;
import com.joe.pay.exception.CheckSignException;
import com.joe.pay.exception.PayException;
import com.joe.pay.wechat.pojo.WxPayParam;
import com.joe.pay.wechat.pojo.WxPayResponse;
import com.joe.pay.wechat.pojo.WxPublicParam;
import com.joe.pay.wechat.pojo.WxPublicResponse;
import com.joe.utils.common.BeanUtils;
import com.joe.utils.common.FormDataBuilder;
import com.joe.utils.common.StringUtils;
import com.joe.utils.common.Tools;
import com.joe.utils.parse.xml.XmlNode;
import com.joe.utils.parse.xml.XmlParser;
import com.joe.utils.secure.MD5;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 微信APP支付服务，文档：https://pay.weixin.qq.com/wiki/doc/api/index.html
 *
 * @author joe
 * @version 2018.06.29 11:31
 */
@Slf4j
public class WxAppPayService implements PayService {
    private static final IHttpClientUtil CLIENT = new IHttpClientUtil();
    private static final XmlParser XML_PARSER = XmlParser.getInstance();
    private static final MD5 MD_5 = new MD5();
    /**
     * appid
     */
    private String appid;
    /**
     * 商户ID
     */
    private String mchId;
    /**
     * 商户平台设置的密钥key
     */
    private String key;

    public static void main(String[] args) {
        WxAppPayService service = new WxAppPayService();
        service.init("", "", "");
        WxPayParam payParam = service.build();
        WxPayResponse response = service.request(payParam, "https://api.mch.weixin.qq.com/pay/unifiedorder",
                WxPayResponse.class);
        System.out.println("结果是：" + response);

    }

    public void init(String appid, String mchId, String key) {
        log.info("初始化微信支付服务，appid:[{}];mchid:[{}];key:[{}}",
                appid,
                StringUtils.replaceAfter(mchId, mchId.length() - 5, "******"),
                StringUtils.replaceAfter(key, key.length() - 5, "******"));
        this.appid = appid;
        this.mchId = mchId;
        this.key = key;
    }

    private WxPayParam build() {
        WxPayParam payParam = new WxPayParam();
        payParam.setAppid(appid);
        payParam.setMchId(mchId);
        payParam.setNonceStr(Tools.createRandomStr(30));
        payParam.setBody("天天爱消除-游戏充值");
        payParam.setOutTradeNo(Tools.createUUID());
        payParam.setTotalFee(100 * 10);
        payParam.setSpbillCreateIp("106.120.141.226");
        payParam.setNotifyUrl("http://wggyl.cn");

        return payParam;
    }

    /**
     * 发送微信请求
     *
     * @param param        请求参数
     * @param url          请求地址
     * @param responseType 响应结果类型Class
     * @param <R>          响应结果实际类型
     * @param <P>          请求参数的实际类型
     * @return 响应结果，当响应结果签名校验异常的时候抛出签名校验异常
     */
    private <R extends WxPublicResponse, P extends WxPublicParam> R request(P param, String url, Class<R>
            responseType) {
        Map<String, Object> map = sign(param);

        String data = XML_PARSER.toXml(map, "xml", true);
        log.debug("待发送数据为[{}]", data);
        try {
            String xmlResponse = CLIENT.executePost(url, data);
            log.debug("响应数据：[{}]", xmlResponse);
            R result = XML_PARSER.parse(xmlResponse, responseType);
            if (!checkSign(result)) {
                throw new CheckSignException("请求结果签名校验异常");
            }
            return result;
        } catch (PayException e) {
            log.error("请求数据[{}]，url：[{}]对应的响应签名校验异常", data, url, e);
            throw e;
        } catch (Throwable e) {
            log.error("请求数据[{}]，url：[{}]请求异常", data, url, e);
            return null;
        }
    }

    /**
     * 校验结果签名
     *
     * @param result 微信请求结果
     * @param <R>    结果实际类型
     * @return 校验签名结果，true表示签名校验通过
     */
    private <R extends WxPublicResponse> boolean checkSign(R result) {
        Map<String, Object> map = sign(result);
        String sysSign = String.valueOf(map.get("sign"));
        String resultSign = result.getSign();
        if (sysSign.equals(resultSign)) {
            return true;
        } else {
            log.warn("系统签名为：[{}]；响应签名为：[{}]，签名不一致", sysSign, resultSign);
            return false;
        }
    }

    /**
     * 签名并返回
     *
     * @param param 要签名的数据
     * @return 将要签名的数据转换为Map并返回，其中包含sign字段
     */
    private Map<String, Object> sign(Object param) {
        log.debug("将数据[{}]转换为待签名的map数据", param);
        Map<String, Object> map = BeanUtils.convert(param, XmlNode.class, false);
        log.debug("数据[{}]转换的map数据为[{}]", param, map);
        map.remove("sign");
        String signData = FormDataBuilder.builder(true, map).data();
        log.debug("要签名的数据为（不包含key）：[{}]", signData);
        signData += "&key=" + key;
        String sign = MD_5.encrypt(signData).toUpperCase();
        log.debug("签名为：[{}]", sign);
        map.put("sign", sign);
        return map;
    }
}
