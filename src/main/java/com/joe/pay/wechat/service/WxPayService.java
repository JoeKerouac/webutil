package com.joe.pay.wechat.service;

import com.joe.pay.AbstractPayService;
import com.joe.pay.exception.CheckSignException;
import com.joe.pay.exception.PayException;
import com.joe.pay.pojo.*;
import com.joe.pay.pojo.prop.PayProp;
import com.joe.pay.wechat.pojo.WxPayParam;
import com.joe.pay.wechat.pojo.WxPayResponse;
import com.joe.pay.wechat.pojo.WxPublicParam;
import com.joe.pay.wechat.pojo.WxPublicResponse;
import com.joe.utils.common.*;
import com.joe.utils.parse.xml.XmlNode;
import com.joe.utils.parse.xml.XmlParser;
import com.joe.utils.secure.MD5;
import com.joe.utils.validator.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;

import static com.joe.pay.PayConst.*;

import java.util.Map;

/**
 * 微信支付服务，文档：https://pay.weixin.qq.com/wiki/doc/api/index.html
 *
 * @author joe
 * @version 2018.06.29 11:31
 */
@Slf4j
public class WxPayService extends AbstractPayService {
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
     * 支付异步回调通知地址
     */
    private String notifyUrl;
    /**
     * 支付URL
     */
    private String payUrl;

    public WxPayService(PayProp prop) {
        super(prop);
        this.appid = prop.getAppid();
        this.mchId = prop.getMchId();
        this.key = prop.getKey();
        this.notifyUrl = prop.getNotifyUrl();
        log.info("初始化微信支付服务，appid:[{}];mchid:[{}];key:[{}};notifyUrl:[{}]",
                appid,
                StringUtils.replaceAfter(mchId, mchId.length() - 5, "******"),
                StringUtils.replaceAfter(key, key.length() - 5, "******"),
                notifyUrl);

        this.payUrl = WX_GATEWAY + WX_PAY_METHOD;
    }

    @Override
    public void useSandbox(boolean sandbox) {
        if (sandbox) {
            throw new IllegalArgumentException("当前支付宝支付暂时不支持沙箱模式");
        } else {
            this.payUrl = WX_GATEWAY + WX_PAY_METHOD;
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

        return sysResponse.conver(wxPayResponse -> {
            PayResponse response = new PayResponse();

            if (isSuccess(wxPayResponse)) {
                log.debug("订单[{}]对应的微信支付成功", param);
                response.setCode("SUCCESS");
                response.setInfo(wxPayResponse.getPrepayId());
            } else {
                log.debug("订单[{}]对应的微信支付失败", param);
                response.setCode("FAIL");
                if (StringUtils.isEmpty(wxPayResponse.getResultCode())) {
                    response.setErrCode(wxPayResponse.getReturnCode());
                    response.setErrMsg(wxPayResponse.getReturnMsg());
                } else {
                    response.setErrCode(wxPayResponse.getErrCode());
                    response.setErrMsg(wxPayResponse.getErrCodeDes());
                }
            }
            log.info("订单[{}]对应的微信支付结果为：[{}]", param, response);
            return response;
        });
    }

    @Override
    public SysResponse<RefundResponse> refund(RefundRequest request) {
        return null;
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
        return request(param, payUrl, WxPayResponse.class);
    }

    /**
     * 发送微信请求
     *
     * @param param        请求参数（该方法会设置参数的appid等配置）
     * @param url          请求地址
     * @param responseType 响应结果类型Class
     * @param <R>          响应结果实际类型
     * @param <P>          请求参数的实际类型
     * @return 响应结果，当响应结果签名校验异常的时候抛出签名校验异常
     */
    private <R extends WxPublicResponse, P extends WxPublicParam> SysResponse<R> request(P param, String url, Class<R>
            responseType) {
        log.debug("设置微信请求[{}]的商家信息", param);
        param.setAppid(appid);
        param.setMchId(mchId);
        param.setNonceStr(Tools.createRandomStr(30));
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
            return SysResponse.buildSuccess(result);
        } catch (PayException e) {
            log.error("请求数据[{}]，url：[{}]对应的响应签名校验异常", data, url, e);
            throw e;
        } catch (Throwable e) {
            log.error("请求数据[{}]，url：[{}]请求异常", data, url, e);
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
        return "SUCCESS".equals(result.getReturnCode()) && "SUCCESS".equals(result.getResultCode());
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
