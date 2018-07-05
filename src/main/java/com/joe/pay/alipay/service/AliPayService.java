package com.joe.pay.alipay.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joe.pay.AbstractPayService;
import com.joe.pay.PayConst;
import com.joe.pay.alipay.pojo.AliAppPayParam;
import com.joe.pay.alipay.pojo.AliPublicParam;
import com.joe.pay.alipay.pojo.AliPublicResponse;
import com.joe.pay.alipay.pojo.BizContent;
import com.joe.pay.pojo.PayParam;
import com.joe.pay.pojo.PayProp;
import com.joe.pay.pojo.PayResponse;
import com.joe.pay.pojo.SysResponse;
import com.joe.utils.common.BeanUtils;
import com.joe.utils.common.DateUtil;
import com.joe.utils.common.FormDataBuilder;
import com.joe.utils.common.Tools;
import com.joe.utils.parse.json.JsonParser;
import com.joe.utils.secure.RSA;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.joe.utils.validator.ValidatorUtil.validate;

/**
 * 阿里支付服务
 *
 * @author joe
 * @version 2018.06.28 15:10
 */
@Slf4j
public class AliPayService extends AbstractPayService {
    private static final JsonParser JSON_PARSER = JsonParser.getInstance();
    private RSA rsa;
    /**
     * 支付宝appid
     */
    private String appid;
    /**
     * 支付宝商户私钥
     */
    private String privateKey;
    /**
     * 回调URL
     */
    private String notifyUrl;
    /**
     * 支付宝网关
     */
    private String gateway;

    public AliPayService(PayProp prop) {
        super(prop);
        this.appid = prop.getAppid();
        this.privateKey = prop.getKey();
        this.rsa = new RSA(privateKey, RSA.RSAType.SHA256WithRSA);
        this.notifyUrl = prop.getNotifyUrl();
        this.gateway = PayConst.ALI_GATEWAY;
    }

    @Override
    public void useSandbox(boolean sandbox) {
        if (sandbox) {
            throw new IllegalArgumentException("当前支付宝支付暂时不支持沙箱模式");
        } else {
            this.gateway = PayConst.ALI_GATEWAY;
        }
    }

    @Override
    public PayResponse pay(PayParam param) {
        AliAppPayParam aliAppPayParam = new AliAppPayParam();
        aliAppPayParam.setBody(param.getBody());
        aliAppPayParam.setSubject(param.getSubject());
        aliAppPayParam.setOutTradeNo(param.getOutTradeNo());
        aliAppPayParam.setTimeoutExpress(Integer.toString(param.getExpire() / 60) + "m");
        aliAppPayParam.setTotalAmount(Tools.dealDouble(param.getTotalAmount() / 100.0));
        aliAppPayParam.setPassbackParams(param.getAttach());

        return pay(aliAppPayParam);
    }

    /**
     * 发起订单（生成订单信息，如果{@link #pay(PayParam) pay}方法不能满足外部系统那么可以调用该方法）
     *
     * @param payParam 阿里订单
     * @return 结果
     */
    public PayResponse pay(AliAppPayParam payParam) {
        log.debug("阿里订单请求[{}]", payParam);
        validate(payParam);

        AliPublicParam publicParam = buildPublicParam();
        publicParam.setBizContent(JSON_PARSER.toJson(payParam));
        Map<String, Object> map = sign(publicParam);
        String data = JSON_PARSER.toJson(map);

        PayResponse response = new PayResponse();
        response.setCode("SUCCESS");
        response.setInfo(data);
        log.info("阿里订单请求[{}]结果：[{}]", publicParam, response);
        return response;
    }

    /**
     * 构建支付宝请求公共参数
     *
     * @return 支付宝请求公共参数
     */
    private AliPublicParam buildPublicParam() {
        AliPublicParam publicParam = new AliPublicParam();
        publicParam.setAppId(appid);
        publicParam.setMethod("alipay.trade.app.pay");
        publicParam.setFormat("JSON");
        publicParam.setTimestamp(DateUtil.getFormatDate(DateUtil.BASE));
        publicParam.setNotifyUrl(notifyUrl);
        return publicParam;
    }

    /**
     * 发起请求
     *
     * @param content 业务参数
     * @param type    响应类型class
     * @param <T>     响应类型
     * @return 响应，请求异常时返回null
     */
    private <T extends AliPublicResponse> SysResponse<T> request(BizContent content, Class<T> type) {
        log.debug("发起支付宝请求，业务参数为[{}]", content);
        AliPublicParam param = buildPublicParam();
        param.setBizContent(JSON_PARSER.toJson(content));

        //签名
        Map<String, Object> map = sign(param);

        String data = JSON_PARSER.toJson(map);
        log.debug("要发送的数据为：[{}]", data);
        try {
            String result = CLIENT.executePost(gateway, data);
            log.debug("请求数据[{}]的请求结果为：[{}]", result);
            T t = JSON_PARSER.readAsObject(result, type);
            return SysResponse.buildSuccess(t);
        } catch (Throwable e) {
            log.error("请求[{}]数据[{}]请求失败", gateway, data, e);
            return SysResponse.buildError(e);
        }
    }

    /**
     * 签名
     *
     * @param param 要签名的参数
     * @return 签名
     */
    private Map<String, Object> sign(Object param) {
        log.debug("将数据[{}]转换为待签名的map数据", param);
        //将参数转换为map并删除sign字段
        Map<String, Object> map = BeanUtils.convert(param, JsonProperty.class, false);
        map.remove("sign");
        log.debug("数据[{}]转换的map数据为[{}]", param, map);
        String signData = FormDataBuilder.builder(true, map).data();
        log.debug("要签名的数据为：[{}]", signData);
        String sign = rsa.encrypt(signData);
        log.debug("签名为：[{}]", sign);
        map.put("sign", sign);
        return map;
    }
}
