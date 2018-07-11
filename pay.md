# 史上最简单的支付SDK
想必很多人在第一次做微信/支付宝支付（后简称支付）的时候都会有一段痛不欲生的经历，官方给的SDK太难用，文档给的又太坑，有时候都是过时的文档，看着文档开发会发现很多跟实际情况都不一样，还要结合着官方的SDK来看，那么有没有这么一个工具包，整合一下两种支付呢？好消息是：有的！！现在就有这么一个工具包，整合了微信/支付宝支付，方便开发者快速接入支付，最最最重要的是：API超级简单！！！

首先我们先看下API：
```java
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
```
可以看到，PayService接口提供了支付方法和退款方法，下面我们看下支付方法的参数定义：
```java
package com.joe.pay.pojo;

import lombok.Data;

/**
 * 支付参数，除了openid外所有参数必填
 *
 * @author joe
 * @version 2018.07.02 10:36
 */
@Data
public class PayRequest implements Request{
    /**
     * 商户网站唯一订单号
     */
    private String outTradeNo;
    /**
     * 对一笔交易的描述信息
     */
    private String body;
    /**
     * 商品的标题/交易标题/订单标题/订单关键字等。
     * <p>
     * 对于微信支付来说需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
     */
    private String subject;
    /**
     * 订单生成时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private String createTime;
    /**
     * 支付时长，单位为秒，如果设置300就是五分钟，超过五分钟订单自动取消
     */
    private int expire = 300;
    /**
     * 订单金额，单位为分
     */
    private int totalAmount;
    /**
     * 发起请求的用户的实际IP
     */
    private String ip;
    /**
     * 支付类型：APP、WEB（微信对应公众号，支付宝对应网页支付）
     */
    private String tradeType = "APP";
    /**
     * 用户openid，只有在微信公众号支付时需要
     */
    private String openid;
    /**
     * 附加数据，会与结果一并返回
     */
    private String attach;
}
```
OK，上边的参数相信就算没做过支付的同学也能很清晰直观的看懂这些信息，都是一些订单基本信息，下面我们来看退款参数的定义：
```java
package com.joe.pay.pojo;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * 退款请求
 *
 * @author joe
 * @version 2018.07.05 17:12
 */
@Data
public class RefundRequest {
    /**
     * 第三方账单ID，与outTradeNo不能同时为空
     */
    private String orderId;
    /**
     * 商户系统订单ID，与orderId不能同时为空
     */
    private String outTradeNo;
    /**
     * 商户退款单号
     * <p>
     * 退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。申请退款总金
     * <p>
     * 额不能超过订单金额。 一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号。
     */
    @NotEmpty(message = "商户退款单号不能为空")
    private String outRefundNo;
    /**
     * 订单总金额，单位为分
     */
    @Size(min = 1)
    private int totalFee;
    /**
     * 退款金额，不能大于订单总金额
     * <p>
     * 注：多次退款总金额不能大于订单总金额
     */
    @Size(min = 1)
    private int refundFee;
    /**
     * 退款原因
     * <p>
     * 可以为空
     */
    private String refundDesc;
}
```
退款参数就更简单了，需要的信息也是相当少。有了这些之后我们就可以来看支付测试代码了：
```java
package com.joe.pay;

import com.joe.pay.pojo.*;
import com.joe.pay.pojo.prop.PayProp;
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
public class PayServiceTest {
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
        wxProp = PayProp.builder()
                .appid("123123123")
                .notifyUrl("http://www.baidu.com")
                .useWxPay()
                .certInput(null)
                .key("123123123")
                .mchId("123123123123")
                .password("123123123123")
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
        aliProp = PayProp.builder()
                .appid("123123123")
                .notifyUrl("http://www.baidu.com")
                .useAliPay()
                .publicKey("123123")
                .privateKey(privateKey)
                .build();
        aliPayService = PayServiceFactory.getInstance(aliProp);
    }

    /**
     * 测试微信支付
     */
    @Test
    public void doWxPay() {
        doPay(wxPayService);
    }

    /**
     * 测试支付宝支付
     */
    @Test
    public void doAliPay() {
        doPay(aliPayService);
    }

    /**
     * 测试微信退款
     * <p>
     * 注意：该测试当前通不过，因为没有提供正确的证书，若想要测试通过则需要在配置文件中提供正确的微信证书和单号
     */
    @Test
    public void doWxRefund() {
        doRefund(wxPayService);
    }

    /**
     * 测试支付宝退款
     * <p>
     * 注意：该测试当前通不过，若想要测试通过则需要提供正确的单号
     */
    @Test
    public void doAliRefund() {
        doRefund(aliPayService);
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
```
可以看到上边测试代码中除了构建支付服务有些许差异外其他的都一模一样（具体的配置参数可以自己在源代码中查看）！！！

看到这里，有的同学可能会说，你这个调用参数太简单了，如果我要用的某个参数你这接口没提供我不就不能用了？NO！！！对于这种用户，我们也提供了解决方案，如果`PayService`接口提供的参数无法满足您的需求，那么微信和支付宝对应的`PayService`实现类中重载了`PayService`接口中所有方法，而且参数是全量的，官方所能用的所有参数这里都有！！！你只需要将`PayService`强转为对应的实现类即可调用，重载方法具体参数可以自己查看源码，这里就不一一说明了。

注：
- 当前仅有支付和退款接口，其他接口正在开发中。

[快速构建web项目能力（基于spring-boot的spring-mvc/jersey项目）](web.md)