package com.joe.pay;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.joe.pay.pojo.*;
import com.joe.pay.pojo.prop.PayProp;
import com.joe.utils.common.DateUtil;
import com.joe.utils.common.Tools;
import com.joe.utils.test.BaseTest;

/**
 * 支付测试
 *
 * @author joe
 * @version 2018.07.02 14:38
 */
public class PayServiceTest extends BaseTest {
    /**
     * 微信配置
     */
    private PayProp    wxProp;
    private PayProp    aliProp;
    /**
     * 微信服务
     */
    private PayService wxPayService;
    private PayService aliPayService;

    /**
     * 测试微信支付
     */
    @Test
    public void doWxPay() {
        runCase(() -> doPay(wxPayService));
    }

    /**
     * 测试支付宝支付
     */
    @Test
    public void doAliPay() {
        runCase(() -> doPay(aliPayService));
    }

    /**
     * 测试微信退款
     * <p>
     * 注意：该测试当前通不过，因为没有提供正确的证书，若想要测试通过则需要在配置文件中提供正确的微信证书和单号
     */
    @Test
    public void doWxRefund() {
        runCase(() -> doRefund(wxPayService));
    }

    /**
     * 测试支付宝退款
     * <p>
     * 注意：该测试当前通不过，若想要测试通过则需要提供正确的单号
     */
    @Test
    public void doAliRefund() {
        runCase(() -> doRefund(aliPayService));
    }

    @Before
    public void junitTest() {
        skipAll(true);
    }

    @Override
    protected void init() {
        //配置微信支付，需要将下列参数替换为自己的参数
        wxProp = PayProp.builder().appid("123123123").notifyUrl("http://www.baidu.com").useWxPay()
            .certInput(null).key("123123123").mchId("123123123123").password("123123123123")
            .build();

        wxPayService = PayServiceFactory.getInstance(wxProp);

        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDEuQvaAad8+PAUVLeh9tqNsWNDCiAXEal4krM0E1"
                            + "4oHiUvzOT8Xf5BYZeMmiA+G+Z4iT2dP6bNRBIth7W5COh41v8TDf4dYxqV3Jw7jyQF2K/p74izFXm96yDXz3cMotRdDW3JdcC"
                            + "ME2tgrlSjJxN26qPCrHh+OL9Y/yqJGhsAHKlaIYQ4S8F9B3rIPMAv6MbTuHFrFXkwuJqesupTwUqzmPgriy/2Au9ZApnpVCex"
                            + "sKIkjMwpmOe3FxaQ9U6271NA9jmzbg7ge5BlfblQL3lIkEQdoqPPKrfLbO3djN1ORn1vhKrIAUhIQAqWKDVdz9dGxrIZckGOe"
                            + "8iuUGOs8D4VAgMBAAECggEBAIzyuJcekE5uXjVy7Y9SOw1Ch4hE/PEKao5FLbimF9ROpP42o+pdvgpCTj8jPu7BNsQuMMM2E+"
                            + "EGYK/WiFFnHIlYIbIZWHTdyPKO+jGCQaEevAu04BDP1kZI7WMy9m1LTTTOplat06OJVmoS/flXyg5t159ny31EU3UEfgl1dts"
                            + "AayODTTRkRUjYXYXSOSDLZjlxSntNS9seklPgEeinc2umrdzJuzciKspK1XLqpCvc+1WA2NlTkoW6Hc9aj61ySCOTkF/3OVjj"
                            + "G58jiUMzSeRsG3gq3PEJfhw2FUiJxn04ZRId74tIKrjKV4Np97WdmFEkpTEUTqvSp3AUYm/wJ5ECgYEA9pzaM+dd7vacLbgkA"
                            + "e3N8dfyJggSJWm5TpangYIfQBXp+TiLhHFP6lhpCrs/qoxdsmM6LtzEkchvAQOA1kgNgRhm4s1pihKc7aAqK9YxE2tT5Z8XXd"
                            + "9SXdLoHXe7GW3Q2ivouMV+J31mhOwSlogR5q0AztXJCl0FH9HHehMyAC8CgYEAzDYJOTOiHm5xjAJ337dtVe1zGUUBwv58B2O"
                            + "wZXp129frnOepM+B663aMbrh+IUzVSQWqufGKUkLwjbRmwjrs9Lw4nFYtNGYVp1wnFawVOXy532tthYG5m3i/KcXjIRwnNHew"
                            + "9Xh4q9rDc30yPKMps+Uijrp7ktQIRfh+QbOi8PsCgYBQccXz3GsVwjgb6c9FGc9uLmT85vEUZzJdYmNtqYaMHxndhAZuw4+v/"
                            + "/rz1zTjazo9MIUQOE+exmT/Tac/Pu24yL73CM5+jafHE9HtzsbfcMlDQ+wZYPm7RUsWIbJUjy/rmxsk+rc2Jn/EqEU/+U7tkk"
                            + "2LKs2TpdhAEuwg9BYBXQKBgQCKAZKmJ7DOJBF0b06X0ByBz6DTWQFhyB4F2GcjjWkNE5TVSvHcbz5i1pD9Wo1S79vMJ5pDY2r"
                            + "5QOfUvDAd3zi4BGO1s7+FA+BqZCq9yLfnS9VJmg5ABXVsDmQSVPu0KpSMLr9WhP34FjW0XM2QkSvjuVxrXLeaeNTGhLK+sCnT"
                            + "9QKBgHfd3hhLWjO4zh99B+PRYFgpnJa9S5E1zzoejZVIJhK5q60KLV5n/HMxuHTGRZxpbfcH4/44d3Yz7ieccmCiPtqxFYUwP"
                            + "W0JukNWDL2tOzTSZ5ABAwqorV3bM67mLUSt0O5dL4YtSBJ2J3F6joO1fTZEcUCybO/A5J4wTZDOV1AR";

        //配置支付宝支付，需要将下列参数替换为自己的参数
        aliProp = PayProp.builder().appid("123123123").notifyUrl("http://www.baidu.com").useAliPay()
            .publicKey("123123").privateKey(privateKey).build();
        aliPayService = PayServiceFactory.getInstance(aliProp);
    }

    /**
     * 校验响应
     *
     * @param response 响应
     */
    private void check(SysResponse<? extends BizResponse> response) {
        Assert.assertTrue(response.isSuccess());
        Assert.assertTrue(response.getData().isSuccess());
    }

    /**
     * 发起支付请求
     *
     * @param service service
     */
    private void doPay(PayService service) {
        SysResponse<PayResponse> response = service.pay(buildPay());
        check(response);
    }

    /**
     * 发起退款请求
     *
     * @param service service
     */
    private void doRefund(PayService service) {
        SysResponse<RefundResponse> response = service.refund(buildRefund());
        System.out.println("结果是：" + response);
    }

    /**
     * 构建一个支付订单
     *
     * @return 支付订单
     */
    private PayRequest buildPay() {
        PayRequest payRequest = new PayRequest();
        payRequest.setOutTradeNo(Tools.createUUID());
        payRequest.setBody("天天爱消除-游戏充值");
        payRequest.setSubject("天天爱消除-游戏充值");
        payRequest.setCreateTime(DateUtil.getFormatDate(DateUtil.BASE));
        payRequest.setTotalAmount(100 * 10);
        payRequest.setIp("106.120.141.226");
        return payRequest;
    }

    /**
     * 构建一个退款订单
     *
     * @return 退款订单
     */
    private RefundRequest buildRefund() {
        RefundRequest request = new RefundRequest();
        request.setOutTradeNo("123456");
        request.setOutRefundNo("123456");
        request.setTotalFee(100 * 10);
        request.setRefundFee(100);
        return request;
    }
}
