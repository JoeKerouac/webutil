package com.joe.pay;

import com.joe.pay.pojo.PayParam;
import com.joe.pay.pojo.PayProp;
import com.joe.pay.pojo.PayResponse;
import com.joe.utils.common.DateUtil;
import com.joe.utils.common.Tools;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 支付测试
 *
 * @author joe
 * @version 2018.07.02 14:38
 */
public class PayTest {
    /**
     * 微信配置
     */
    private PayProp wxProp;
    private PayProp aliProp;
    /**
     * 微信服务
     */
    private PayService wxPayService;
    private PayService aliPayService;

    @Before
    public void init() {
        //配置微信支付，需要将下列参数替换为自己的参数
        wxProp = new PayProp();
        wxProp.setAppid("123123123");
        wxProp.setKey("123123123");
        wxProp.setMchId("123123123");
        wxProp.setNotifyUrl("http://baidu.com");
        wxProp.setMode(PayProp.PayMode.WECHAT);
        wxPayService = PayServiceFactory.getInstance(wxProp);

        //配置支付宝支付，需要将下列参数替换为自己的参数
        aliProp = new PayProp();
        aliProp.setAppid("123");
        aliProp.setKey("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDEuQvaAad8+PAUVLeh9tqNsWNDCiAXEal4krM0E1" +
                "4oHiUvzOT8Xf5BYZeMmiA+G+Z4iT2dP6bNRBIth7W5COh41v8TDf4dYxqV3Jw7jyQF2K/p74izFXm96yDXz3cMotRdDW3JdcC" +
                "ME2tgrlSjJxN26qPCrHh+OL9Y/yqJGhsAHKlaIYQ4S8F9B3rIPMAv6MbTuHFrFXkwuJqesupTwUqzmPgriy/2Au9ZApnpVCex" +
                "sKIkjMwpmOe3FxaQ9U6271NA9jmzbg7ge5BlfblQL3lIkEQdoqPPKrfLbO3djN1ORn1vhKrIAUhIQAqWKDVdz9dGxrIZckGOe" +
                "8iuUGOs8D4VAgMBAAECggEBAIzyuJcekE5uXjVy7Y9SOw1Ch4hE/PEKao5FLbimF9ROpP42o+pdvgpCTj8jPu7BNsQuMMM2E+" +
                "EGYK/WiFFnHIlYIbIZWHTdyPKO+jGCQaEevAu04BDP1kZI7WMy9m1LTTTOplat06OJVmoS/flXyg5t159ny31EU3UEfgl1dts" +
                "AayODTTRkRUjYXYXSOSDLZjlxSntNS9seklPgEeinc2umrdzJuzciKspK1XLqpCvc+1WA2NlTkoW6Hc9aj61ySCOTkF/3OVjj" +
                "G58jiUMzSeRsG3gq3PEJfhw2FUiJxn04ZRId74tIKrjKV4Np97WdmFEkpTEUTqvSp3AUYm/wJ5ECgYEA9pzaM+dd7vacLbgkA" +
                "e3N8dfyJggSJWm5TpangYIfQBXp+TiLhHFP6lhpCrs/qoxdsmM6LtzEkchvAQOA1kgNgRhm4s1pihKc7aAqK9YxE2tT5Z8XXd" +
                "9SXdLoHXe7GW3Q2ivouMV+J31mhOwSlogR5q0AztXJCl0FH9HHehMyAC8CgYEAzDYJOTOiHm5xjAJ337dtVe1zGUUBwv58B2O" +
                "wZXp129frnOepM+B663aMbrh+IUzVSQWqufGKUkLwjbRmwjrs9Lw4nFYtNGYVp1wnFawVOXy532tthYG5m3i/KcXjIRwnNHew" +
                "9Xh4q9rDc30yPKMps+Uijrp7ktQIRfh+QbOi8PsCgYBQccXz3GsVwjgb6c9FGc9uLmT85vEUZzJdYmNtqYaMHxndhAZuw4+v/" +
                "/rz1zTjazo9MIUQOE+exmT/Tac/Pu24yL73CM5+jafHE9HtzsbfcMlDQ+wZYPm7RUsWIbJUjy/rmxsk+rc2Jn/EqEU/+U7tkk" +
                "2LKs2TpdhAEuwg9BYBXQKBgQCKAZKmJ7DOJBF0b06X0ByBz6DTWQFhyB4F2GcjjWkNE5TVSvHcbz5i1pD9Wo1S79vMJ5pDY2r" +
                "5QOfUvDAd3zi4BGO1s7+FA+BqZCq9yLfnS9VJmg5ABXVsDmQSVPu0KpSMLr9WhP34FjW0XM2QkSvjuVxrXLeaeNTGhLK+sCnT" +
                "9QKBgHfd3hhLWjO4zh99B+PRYFgpnJa9S5E1zzoejZVIJhK5q60KLV5n/HMxuHTGRZxpbfcH4/44d3Yz7ieccmCiPtqxFYUwP" +
                "W0JukNWDL2tOzTSZ5ABAwqorV3bM67mLUSt0O5dL4YtSBJ2J3F6joO1fTZEcUCybO/A5J4wTZDOV1AR");
        aliProp.setNotifyUrl("http://baidu.com");
        aliProp.setMode(PayProp.PayMode.ALIAPP);
        aliPayService = PayServiceFactory.getInstance(aliProp);
    }

    /**
     * 测试微信支付
     */
    @Test
    public void doWxPay() {
        PayParam param = build();
        PayResponse response = wxPayService.pay(param);
        Assert.assertEquals("SUCCESS", response.getCode());
    }

    /**
     * 测试微信支付
     */
    @Test
    public void doAliPay() {
        PayParam param = build();
        PayResponse response = aliPayService.pay(param);
        Assert.assertEquals("SUCCESS", response.getCode());
    }

    /**
     * 构建一个订单
     *
     * @return 订单
     */
    private PayParam build() {
        PayParam payParam = new PayParam();
        payParam.setOutTradeNo(Tools.createUUID());
        payParam.setBody("天天爱消除-游戏充值");
        payParam.setSubject("天天爱消除-游戏充值");
        payParam.setCreateTime(DateUtil.getFormatDate(DateUtil.BASE));
        payParam.setTotalAmount(100 * 10);
        payParam.setIp("106.120.141.226");
        return payParam;
    }
}
