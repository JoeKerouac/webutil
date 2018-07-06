package com.joe.pay;

import com.joe.pay.pojo.*;

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
    SysResponse<PayResponse> pay(PayRequest request);

    /**
     * 申请退款
     *
     * @param request 退款请求
     * @return 退款响应
     */
    SysResponse<RefundResponse> refund(RefundRequest request);
}
