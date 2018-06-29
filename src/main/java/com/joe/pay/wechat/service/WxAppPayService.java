package com.joe.pay.wechat.service;

import com.joe.http.IHttpClientUtil;
import com.joe.pay.PayService;
import com.joe.pay.wechat.pojo.PayParam;
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
        PayParam payParam = service.build();
        service.request(payParam, "https://api.mch.weixin.qq.com/pay/unifiedorder", null);
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

    private PayParam build() {
        PayParam payParam = new PayParam();
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
     * 发送请求
     *
     * @param param        请求参数
     * @param url          请求地址
     * @param responseType 响应结果类型Class
     * @param <T>          响应结果实际类型
     * @return 响应结果
     */
    private <T> T request(PayParam param, String url, Class<T> responseType) {
        log.debug("将数据[{}]转换为map数据", param);
        Map<String, Object> map = BeanUtils.convert(param, XmlNode.class, false);
        log.debug("数据[{}]转换的map数据为[{}]", param, map);
        map.remove("sign");
        String signData = FormDataBuilder.builder(true, map).data();
        log.debug("要签名的数据为（不包含key）：[{}]", signData);
        signData += "&key=" + key;
        String sign = MD_5.encrypt(signData).toUpperCase();
        log.debug("签名为：[{}]", sign);
        map.put("sign", sign);
        String data = XML_PARSER.toXml(map, "xml", true);
        log.debug("待发送数据为[{}]", data);

        try {
            String xmlResponse = CLIENT.executePost(url, data);
            log.debug("响应数据：[{}]", xmlResponse);
            T t = XML_PARSER.parse(xmlResponse, responseType);
            return t;
        } catch (Throwable e) {
            log.error("请求数据[{}]，url：[{}]请求异常", data, e);
            return null;
        }
    }
}
