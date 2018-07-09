package com.joe.pay.wechat.service;

import com.joe.http.IHttpClientUtil;
import com.joe.http.client.IHttpClient;
import com.joe.http.common.SSLTools;
import com.joe.pay.AbstractPayService;
import com.joe.pay.exception.CheckSignException;
import com.joe.pay.exception.PayException;
import com.joe.pay.pojo.*;
import com.joe.pay.pojo.prop.WxPayProp;
import com.joe.pay.wechat.pojo.*;
import com.joe.utils.common.*;
import com.joe.utils.parse.xml.XmlNode;
import com.joe.utils.parse.xml.XmlParser;
import com.joe.utils.secure.MD5;
import com.joe.utils.validator.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Function;

import static com.joe.pay.PayConst.*;
import static com.joe.utils.validator.ValidatorUtil.validate;

/**
 * 微信支付服务，文档：https://pay.weixin.qq.com/wiki/doc/api/index.html
 *
 * @author joe
 * @version 2018.06.29 11:31
 */
@Slf4j
public class WxPayService extends AbstractPayService {
    protected IHttpClientUtil wxClient;
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
    /**
     * 是否支持退款（true表示支持）
     */
    private boolean supportRefund = false;
    /**
     * 支付异步回调通知地址
     */
    private String notifyUrl;
    /**
     * 微信网关
     */
    private String gateway;
    /**
     * 支付方法
     */
    private String payMethod;
    /**
     * 退款方法
     */
    private String refundMethod;

    public WxPayService(WxPayProp prop) {
        super(prop);
        this.appid = prop.getAppid();
        this.mchId = prop.getMchId();
        this.key = prop.getKey();
        this.notifyUrl = prop.getNotifyUrl();
        //微信需要采用需要证书
        if (prop.getCertInput() == null || StringUtils.isEmpty(prop.getPassword())) {
            this.wxClient = DEFAULT_CLIENT;
            supportRefund = false;
            log.warn("当前没有提供微信证书或者证书密码为空，不能使用退款接口");
        } else {
            IHttpClient client = IHttpClient.builder()
                    .sslcontext(SSLTools.build(prop.getCertInput(), "PKCS12", prop.getPassword()))
                    .build();
            this.wxClient = new IHttpClientUtil(client);
            supportRefund = true;
        }
        log.info("初始化微信支付服务，appid:[{}];mchid:[{}];key:[{}};notifyUrl:[{}]",
                appid,
                StringUtils.replaceAfter(mchId, mchId.length() - 5, "******"),
                StringUtils.replaceAfter(key, key.length() - 5, "******"),
                notifyUrl);
        useSandbox(false);
    }

    @Override
    public void useSandbox(boolean sandbox) {
        if (sandbox) {
            throw new IllegalArgumentException("当前支付宝支付暂时不支持沙箱模式");
        } else {
            this.gateway = WX_GATEWAY;
            this.payMethod = WX_PAY_METHOD;
            this.refundMethod = WX_REFUND_METHOD;
        }
    }

    @Override
    public SysResponse<PayResponse> pay(PayRequest param) {
        log.debug("调用微信支付，支付参数为：[{}]", param);
        WxPayParam wxPayParam = new WxPayParam();
        wxPayParam.setBody(param.getSubject());
        wxPayParam.setDetail(param.getBody());
        wxPayParam.setOutTradeNo(param.getOutTradeNo());
        wxPayParam.setTotalFee(param.getTotalAmount());
        wxPayParam.setSpbillCreateIp(param.getIp());
        wxPayParam.setTimeStart(param.getCreateTime().replaceAll("-", "").replace(" ", "").replace(":", ""));
        wxPayParam.setTimeExpire(DateUtil.getFormatDate("yyyyMMddHHmmss", DateUtil.add(DateUtil.DateUnit.SECOND, param
                .getExpire(), param.getCreateTime(), DateUtil.BASE)));
        wxPayParam.setNotifyUrl(notifyUrl);
        wxPayParam.setOpenid(param.getOpenid());
        wxPayParam.setTradeType(param.getTradeType());
        wxPayParam.setAttach(param.getAttach());

        log.debug("系统订单[{}]转换为了微信订单：[{}]", param, wxPayParam);
        SysResponse<WxPayResponse> sysResponse = pay(wxPayParam);
        log.info("微信支付参数[{}]对应的响应为：[{}]", param, sysResponse);

        return sysResponse.conver(wxPayResponse ->
                convert(wxPayResponse, new PayResponse(), payResponse -> {
                    log.debug("订单[{}]对应的微信支付成功", param);
                    PayResponse response = new PayResponse();
                    response.setInfo(payResponse.getPrepayId());
                    return response;
                }));
    }

    /**
     * 使用微信订单发起支付（如果{@link #pay(PayRequest) pay}方法不能满足外部系统需求，那么可以使用该方法发起支付）
     *
     * @param param 微信订单
     * @return 支付结果
     */
    public SysResponse<WxPayResponse> pay(WxPayParam param) {
        log.debug("发起微信支付，支付参数为：[{}]", param);
        ValidatorUtil.validate(param);
        return request(param, payMethod, WxPayResponse.class);
    }

    @Override
    public SysResponse<RefundResponse> refund(RefundRequest request) {
        log.debug("对微信系统订单[{}]退款", request);
        WxRefundParam param = new WxRefundParam();
        param.setOutTradeNo(request.getOutTradeNo());
        param.setTransaction_id(request.getOrderId());
        param.setOutRefundNo(request.getOutRefundNo());
        param.setTotalFee(request.getTotalFee());
        param.setRefundFee(request.getRefundFee());
        param.setRefundDesc(request.getRefundDesc());

        SysResponse<WxRefundResponse> sysResponse = refund(param);

        SysResponse<RefundResponse> result = sysResponse.conver(wxRefundResponse ->
                convert(wxRefundResponse, new RefundResponse(), refundResponse -> {
                    RefundResponse response = new RefundResponse();
                    response.setOrderId(refundResponse.getTransactionId());
                    response.setOutTradeNo(refundResponse.getOutTradeNo());
                    response.setRefundFee(refundResponse.getRefundFee());
                    return response;
                })
        );
        log.debug("对微信系统订单[{}]的退款结果是：[{}]", request, result);
        return result;
    }

    /**
     * 调用底层微信退款
     *
     * @param param 微信退款参数
     * @return 微信退款结果
     */
    public SysResponse<WxRefundResponse> refund(WxRefundParam param) {
        if (!supportRefund) {
            throw new RuntimeException("当前没有提供微信证书，不支持退款操作");
        }
        log.debug("对微信订单[{}]退款", param);
        validate(param);
        if (StringUtils.isEmptyAll(param.getOutTradeNo(), param.getTransaction_id())) {
            throw new IllegalArgumentException("商户订单号和微信交易号不能同时为空");
        }

        SysResponse<WxRefundResponse> sysResponse = request(param, refundMethod, WxRefundResponse.class);
        log.debug("微信订单[{}]退款结果为：[{}]", param, sysResponse);
        return sysResponse;
    }


    /**
     * 结果转换，将网络请求的结果转换为系统响应结果
     *
     * @param data            网络请求结果
     * @param r               系统响应结果
     * @param successFunction 成功结果转换函数，将网络请求结果转换为系统响应结果，验证业务成功后调用该函数
     * @param <T>             网络请求结果类型
     * @param <R>             系统响应结果类型
     * @return 系统响应结果
     */
    private <T extends WxPublicResponse, R extends BizResponse> R convert(T data, R r, Function<T, R> successFunction) {
        R result;
        if (isSuccess(data)) {
            result = successFunction.apply(data);
        } else {
            result = r;
        }
        result.setSuccess(isSuccess(data));
        result.setCode(getCode(data));
        result.setErrMsg(getMsg(data));
        return result;
    }

    /**
     * 发送微信请求
     *
     * @param param        请求参数（该方法会设置参数的appid等配置）
     * @param method       请求地址
     * @param responseType 响应结果类型Class
     * @param <R>          响应结果实际类型
     * @param <P>          请求参数的实际类型
     * @return 响应结果，当响应结果签名校验异常的时候抛出签名校验异常
     */
    private <R extends WxPublicResponse, P extends WxPublicParam> SysResponse<R> request(P param, String method,
                                                                                         Class<R>
                                                                                                 responseType) {
        log.debug("设置微信请求[{}]的商家信息", param);
        param.setAppid(appid);
        param.setMchId(mchId);
        param.setNonceStr(Tools.createRandomStr(30));
        Map<String, Object> map = sign(param);

        String data = XML_PARSER.toXml(map, "xml", true);
        String realMethod = gateway + method;
        log.debug("准备往[{}]发送数据为[{}]", realMethod, data);
        try {
            String xmlResponse = wxClient.executePost(realMethod, data);
            log.debug("响应数据：[{}]", xmlResponse);
            R result = XML_PARSER.parse(xmlResponse, responseType);
            if (!checkSign(result)) {
                throw new CheckSignException("请求结果签名校验异常");
            }
            return SysResponse.buildSuccess(result);
        } catch (PayException e) {
            log.error("请求数据[{}]，url：[{}]对应的响应签名校验异常", data, method, e);
            throw e;
        } catch (Throwable e) {
            log.error("请求数据[{}]，url：[{}]请求异常", data, method, e);
            return SysResponse.buildError(e);
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
        if (!isSuccess(result)) {
            //请求不成功，不验签
            return true;
        }
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
     * 微信请求是否成功
     *
     * @param result 请求结果
     * @param <R>    结果实际类型
     * @return 返回true表示成功
     */
    private <R extends WxPublicResponse> boolean isSuccess(R result) {
        return result != null && "SUCCESS".equals(result.getReturnCode()) && "SUCCESS".equals(result.getResultCode());
    }

    /**
     * 获取响应代码
     *
     * @param result 响应结果
     * @param <R>    结果类型
     * @return 响应代码
     */
    private <R extends WxPublicResponse> String getCode(R result) {
        return result.getErrCode() == null ? result.getReturnCode() : result.getErrCode();
    }

    /**
     * 获取响应代码说明
     *
     * @param result 响应结果
     * @param <R>    结果类型
     * @return 响应代码说明
     */
    private <R extends WxPublicResponse> String getMsg(R result) {
        return result.getErrCodeDes() == null ? result.getReturnMsg() : result.getErrCodeDes();
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
        map.remove("sign");
        log.debug("数据[{}]转换的map数据为[{}]", param, map);
        String signData = FormDataBuilder.builder(true, map).data();
        log.debug("要签名的数据为（不包含key）：[{}]", signData);
        signData += "&key=" + key;
        String sign = MD_5.encrypt(signData).toUpperCase();
        log.debug("签名为：[{}]", sign);
        map.put("sign", sign);
        return map;
    }
}
