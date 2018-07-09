package com.joe.pay;

import com.joe.pay.alipay.service.AliPayService;
import com.joe.pay.pojo.prop.AliPayProp;
import com.joe.pay.pojo.prop.PayProp;
import com.joe.pay.pojo.prop.WxPayProp;
import com.joe.pay.wechat.service.WxPayService;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付服务工厂
 *
 * @author joe
 * @version 2018.07.05 09:50
 */
public class PayServiceFactory {
    private static final Map<PayProp, PayService> CACHE = new HashMap<>();

    /**
     * 获取指定的支付服务
     *
     * @param prop 支付配置
     * @return 支付服务
     */
    public static PayService getInstance(PayProp prop) {
        PayService service = CACHE.get(prop);
        if (service != null) {
            return service;
        }

        PayProp.PayMode mode = prop.getMode();

        synchronized (mode) {
            if ((service = CACHE.get(prop)) != null) {
                return service;
            }
            switch (mode) {
                case ALIAPP:
                    service = new AliPayService((AliPayProp) prop);
                    break;
                case WECHAT:
                    service = new WxPayService((WxPayProp) prop);
                    break;
                default:
                    throw new IllegalArgumentException("未知支付模式：" + mode);
            }
        }
        CACHE.put(prop, service);
        return service;
    }
}
