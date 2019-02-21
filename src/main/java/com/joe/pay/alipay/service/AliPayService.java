package com.joe.pay.alipay.service;

import static com.joe.pay.PayConst.*;
import static com.joe.utils.validator.ValidatorUtil.validate;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joe.http.request.IHttpRequestBase;
import com.joe.pay.AbstractPayService;
import com.joe.pay.alipay.pojo.*;
import com.joe.pay.exception.CheckSignException;
import com.joe.pay.pojo.*;
import com.joe.pay.pojo.prop.AliPayProp;
import com.joe.utils.collection.CollectionUtil;
import com.joe.utils.common.DateUtil;
import com.joe.utils.common.FormDataBuilder;
import com.joe.utils.common.StringUtils;
import com.joe.utils.common.Tools;
import com.joe.utils.reflect.BeanUtils;
import com.joe.utils.secure.SignatureUtil;
import com.joe.utils.secure.impl.SignatureUtilImpl;
import com.joe.utils.serialize.json.JsonParser;
import com.joe.utils.validator.ValidatorUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 阿里支付服务
 *
 * @author joe
 * @version 2018.06.28 15:10
 */
@Slf4j
public class AliPayService extends AbstractPayService {
    private static final JsonParser JSON_PARSER = JsonParser.getInstance();
    /**
     * 签名用
     */
    private SignatureUtil           signature;
    /**
     * 支付宝appid
     */
    private String                  appid;
    /**
     * 支付宝商户私钥
     */
    private String                  privateKey;
    /**
     * 公钥
     */
    private String                  publicKey;
    /**
     * 回调URL
     */
    private String                  notifyUrl;
    /**
     * 支付宝网关
     */
    private String                  gateway;
    /**
     * 字符集
     */
    private String                  charset;

    public AliPayService(AliPayProp prop) {
        super(prop);
        this.appid = prop.getAppid();
        this.privateKey = prop.getPrivateKey();
        this.publicKey = prop.getPublicKey();
        this.signature = SignatureUtilImpl.buildInstance(privateKey, publicKey,
            SignatureUtil.Algorithms.SHA256withRSA);
        this.notifyUrl = prop.getNotifyUrl();
        this.charset = Charset.defaultCharset().name();
        useSandbox(false);
    }

    @Override
    public void useSandbox(boolean sandbox) {
        if (sandbox) {
            throw new IllegalArgumentException("当前支付宝支付暂时不支持沙箱模式");
        } else {
            this.gateway = ALI_GATEWAY;
        }
    }

    @Override
    public SysResponse<PayResponse> pay(PayRequest param) {
        AliAppPayParam aliAppPayParam = new AliAppPayParam();
        aliAppPayParam.setBody(param.getBody());
        aliAppPayParam.setSubject(param.getSubject());
        aliAppPayParam.setOutTradeNo(param.getOutTradeNo());
        aliAppPayParam.setTimeoutExpress(Integer.toString(param.getExpire() / 60) + "m");
        aliAppPayParam.setTotalAmount(Tools.dealDouble(param.getTotalAmount() / 100.0));
        aliAppPayParam.setPassbackParams(param.getAttach());

        String data = pay(aliAppPayParam);
        PayResponse response = new PayResponse();
        response.setInfo(data);
        response.setSuccess(true);
        log.info("阿里订单请求[{}]结果：[{}]", param, response);
        return SysResponse.buildSuccess(response);
    }

    /**
     * 发起订单（生成订单信息，如果{@link #pay(PayRequest) pay}方法不能满足外部系统那么可以调用该方法）
     *
     * @param payParam 阿里订单
     * @return 结果
     */
    public String pay(AliAppPayParam payParam) {
        log.debug("阿里订单请求[{}]", payParam);
        validate(payParam);

        AliPublicParam publicParam = buildPublicParam(ALI_PAY_METHOD);
        publicParam.setBizContent(JSON_PARSER.toJson(payParam));
        Map<String, Object> map = sign(publicParam);
        return JSON_PARSER.toJson(map);
    }

    @Override
    public PayNotify payNotify(HttpServletRequest request) {
        return null;
    }

    @Override
    public SysResponse<RefundResponse> refund(RefundRequest request) {
        log.debug("收到阿里支付退款订单：[{}]", request);
        AliRefundParam param = new AliRefundParam();
        param.setTradeNo(request.getOrderId());
        param.setOutTradeNo(request.getOutTradeNo());
        param.setOutRequestNo(request.getOutRefundNo());
        param.setRefundAmount(request.getRefundFee() / 1.0);
        param.setRefundReason(request.getRefundDesc());
        SysResponse<AliRefundResponse> sysResponse = refund(param, null);
        log.debug("阿里系统支付退款订单退款结果：[{}]", sysResponse);

        return sysResponse.conver(aliRefundResponse -> convert(aliRefundResponse,
            new RefundResponse(), refundResponse -> {
                RefundResponse response = new RefundResponse();
                response.setOrderId(refundResponse.getTradeNo());
                response.setOutTradeNo(refundResponse.getOutTradeNo());
                response.setRefundFee((int) (refundResponse.getRefundFee() * 100));
                return response;
            }));
    }

    /**
     * 订单退款
     *
     * @param param           退款详情
     * @param goodsDetailList 退款商品列表，可以为空
     */
    public SysResponse<AliRefundResponse> refund(AliRefundParam param,
                                                 List<AliRefundParam.GoodsDetail> goodsDetailList) {
        log.debug("阿里退款请求:[{}]:[{}]", param, goodsDetailList);
        validate(param);
        if (StringUtils.isEmptyAll(param.getOutTradeNo(), param.getTradeNo())) {
            throw new IllegalArgumentException("商户订单号和支付宝交易号不能同时为空");
        }

        if (!CollectionUtil.safeIsEmpty(goodsDetailList)) {
            goodsDetailList.parallelStream().forEach(ValidatorUtil::validate);
            param.setGoodsDetail(JSON_PARSER.toJson(goodsDetailList));
        }

        SysResponse<AliRefundResponse> sysResponse = request(param, ALI_REFUND_METHOD,
            AliRefundResponseData.class);
        log.debug("阿里支付退款订单退款结果：[{}]", sysResponse);
        return sysResponse;
    }

    /**
     * 处理业务
     *
     * @param data            阿里响应
     * @param r               业务响应
     * @param successFunction 业务成功时的回调
     * @param <T>             阿里响应类型
     * @param <R>             业务数据类型
     * @return 业务响应
     */
    private <T extends AliPublicResponse, R extends BizResponse> R convert(T data, R r,
                                                                           Function<T, R> successFunction) {
        R result;
        if ("10000".equals(data.getCode())) {
            result = successFunction.apply(data);
        } else {
            result = r;
        }
        result.setSuccess("10000".equals(data.getCode()));
        result.setCode(data.getCode());
        result.setErrMsg(data.getMsg());
        return result;
    }

    /**
     * 构建支付宝请求公共参数
     *
     * @param method 请求接口名
     * @return 支付宝请求公共参数
     */
    private AliPublicParam buildPublicParam(String method) {
        AliPublicParam publicParam = new AliPublicParam();
        publicParam.setAppId(appid);
        publicParam.setMethod(method);
        publicParam.setFormat("JSON");
        publicParam.setTimestamp(DateUtil.getFormatDate(DateUtil.BASE));
        publicParam.setNotifyUrl(notifyUrl);
        return publicParam;
    }

    /**
     * 发起请求
     *
     * @param content 业务参数
     * @param method  请求接口
     * @param type    响应类型class
     * @param <T>     响应类型
     * @return 响应，请求异常时返回null
     */
    private <D extends AliResponseData<T>, T extends AliPublicResponse> SysResponse<T> request(BizContent content,
                                                                                               String method,
                                                                                               Class<D> type) {
        log.debug("发起支付宝请求，业务参数为[{}]", content);
        AliPublicParam param = buildPublicParam(method);
        param.setBizContent(JSON_PARSER.toJson(content, true));

        //签名
        Map<String, Object> map = sign(param);

        String data = FormDataBuilder.builder(map).data(true, "UTF8");
        log.debug("要发送的数据为：[{}]", data);
        try {

            String result = DEFAULT_CLIENT.executePost(gateway, data, charset, charset,
                IHttpRequestBase.CONTENT_TYPE_FORM);
            log.debug("请求数据[{}]的请求结果为：[{}]", data, result);
            D responseData = JSON_PARSER.readAsObject(result, type);
            //目前对responseData没有验签逻辑，需要添加验签逻辑
            T t = responseData.getData();
            //校验签名
            checkSign(t, responseData.getSign());
            return SysResponse.buildSuccess(t);
        } catch (Throwable e) {
            log.error("请求[{}]数据[{}]请求失败", gateway, data, e);
            return SysResponse.buildError(e);
        }
    }

    /**
     * 校验网络请求响应签名
     *
     * @param t    响应数据
     * @param sign 响应签名
     * @param <T>  响应数据实际类型
     */
    private <T extends AliPublicResponse> void checkSign(T t, String sign) {
        String data = JSON_PARSER.toJson(t, true);
        boolean check = signature.checkSign(data, sign);
        if (!check && data.contains("\\/")) {
            data = data.replace("\\/", "/");
            check = signature.checkSign(data, sign);
        }
        if (!check) {
            throw new CheckSignException("支付宝响应签名校验异常·");
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
        String sign = signature.sign(signData);
        log.debug("签名为：[{}]", sign);
        map.put("sign", sign);
        return map;
    }
}
