package com.joe.pay;

import com.joe.pay.pojo.PayParam;
import com.joe.pay.pojo.PayProp;
import com.joe.pay.pojo.PayResponse;

/**
 * 支付服务接口
 *
 * @author joe
 * @version 2018.06.29 11:32
 */
public interface PayService {
    /**
     * 初始化服务，调用功能前必须先初始化
     *
     * @param prop 初始化参数
     */
    void init(PayProp prop);

    /**
     * 调用第三方支付
     *
     * @param param 支付参数
     * @return 支付结果
     */
    PayResponse pay(PayParam param);
}
