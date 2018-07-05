package com.joe.pay;

import com.joe.pay.pojo.PayRequest;
import com.joe.pay.pojo.PayResponse;
import com.joe.pay.pojo.RefundRequest;
import com.joe.pay.pojo.RefundResponse;

/**
 * 支付服务接口
 *
 * @author joe
 * @version 2018.06.29 11:32
 */
public interface PayService {
    /**
     * 调用第三方支付
     *
     * @param request 支付参数
     * @return 支付结果
     */
    PayResponse pay(PayRequest request);

    /**
     * 申请退款
     *
     * @param request 退款请求
     * @return 退款响应
     */
    RefundResponse refund(RefundRequest request);
}
