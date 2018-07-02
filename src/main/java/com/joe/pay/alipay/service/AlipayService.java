package com.joe.pay.alipay.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joe.http.IHttpClientUtil;
import com.joe.pay.PayService;
import com.joe.pay.alipay.pojo.AliPublicResponse;
import com.joe.pay.alipay.pojo.BizContent;
import com.joe.pay.alipay.pojo.AliPublicParam;
import com.joe.utils.common.BeanUtils;
import com.joe.utils.common.FormDataBuilder;
import com.joe.utils.common.StringUtils;
import com.joe.utils.parse.json.JsonParser;
import com.joe.utils.secure.RSA;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

/**
 * 阿里支付服务
 *
 * @author joe
 * @version 2018.06.28 15:10
 */
@Slf4j
public class AlipayService implements PayService {
    private static final JsonParser JSON_PARSER = JsonParser.getInstance();
    private static final IHttpClientUtil CLIENT = new IHttpClientUtil();
    private String url;
    private RSA rsa;
    private String appid;
    private String privateKey;

    /**
     * 初始化函数
     *
     * @param appid      appid
     * @param privateKey 商户私钥
     */
    public void init(String appid, String privateKey) {
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(privateKey)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        this.appid = appid;
        this.privateKey = privateKey;
        this.rsa = new RSA(privateKey, RSA.RSAType.SHA256WithRSA);

    }

    /**
     * 发起请求
     *
     * @param param   公共参数
     * @param content 业务参数
     * @param type    响应类型class
     * @param <T>     响应类型
     * @return 响应，请求异常时返回null
     */
    private <T extends AliPublicResponse> T request(AliPublicParam param, BizContent content, Class<T> type) {
        log.debug("发起请求，公共参数为[{}]，业务参数为[{}]", param, content);
        param.setBizContent(JSON_PARSER.toJson(content));

        //将参数转换为map并删除sign字段
        Map<String, String> map = BeanUtils.convert(param, JsonProperty.class, false);
        map.remove("sign");

        log.debug("排序并构建签名数据");
        String signData = FormDataBuilder.builder(true, map).data();
        log.debug("要签名的数据为：[{}]", signData);
        String sign = rsa.encrypt(signData);
        log.debug("签名为：[{}]", sign);
        map.put("sign", sign);

        String data = JSON_PARSER.toJson(map);
        log.debug("要发送的数据为：[{}]", data);

        try {
            String result = CLIENT.executePost(url, data);
            log.debug("请求数据[{}]的请求结果为：[{}]", result);
            T t = JSON_PARSER.readAsObject(result, type);
            return t;
        } catch (Throwable e) {
            log.error("请求[{}]数据[{}]请求失败", url, data, e);
            return null;
        }
    }

    /**
     * 签名
     *
     * @param param 要签名的参数
     * @return 签名
     */
    public String sign(AliPublicParam param) {
        TreeMap<String, String> map = JSON_PARSER.readAsMap(JSON_PARSER.toJson(param, true), TreeMap.class,
                String.class, String.class);
        String formData = FormDataBuilder.builder(map).data();
        return rsa.encrypt(formData);
    }
}
