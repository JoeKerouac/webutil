# 项目说明
该项目是web框架，用于快速构建web项目，集成了spring-boot，对spring-boot简单的封装了一下，同时提供了jersey集成。

# 项目功能
- [快速构建web项目（基于spring-boot的spring-mvc/jersey项目）](#快速构建web项目)
- [提供微信支付/支付宝支付能力](#支付能力)

## 快速构建web项目
### 使用spring-mvc
首先声明一个Controller
```java
package com.joe.web.starter.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * springMVC控制器
 *
 * @author joe
 * @version 2018.03.09 18:49
 */
@Controller
public class Api {
    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String test() {
        return "hello world";
    }
}
```
然后配置启动类
```java
package com.joe.web.starter.test;

import com.joe.web.starter.WebApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 测试主类
 *
 * @author joe
 * @version 2018.03.09 18:49
 */
@ComponentScan("com.joe.web")
public class App {
    public static void main(String[] args) throws Exception {
        WebApplication.runWeb(App.class, args);
    }
}
```
该APP类是一个启动类，上边注明了要扫描com.joe.web包，然后使用WebApplication.runWeb方法启动服务器，启动的时候需要将该类传入同时将系统参数args传入即可。
### 使用jersey
可以看到该启动方式和spring提供的SpringApplication.run()没有什么区别，那么本框架做了什么呢？别急，再看下面的例子：
```java
package com.joe.web.starter.test;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author joe
 * @version 2018.03.19 16:33
 */
@Path("jersey")
public class JerseyApi {
    @RolesAllowed("user")
    @GET
    @Path("hello")
    public String hello() {
        return "hello";
    }
}
```
```java
package com.joe.web.starter.test;

import com.joe.web.starter.WebApplication;
import com.joe.web.starter.core.prop.SysProp;
import com.joe.web.starter.core.secure.entity.Role;
import com.joe.web.starter.core.secure.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;

/**
 * 测试主类
 *
 * @author joe
 * @version 2018.03.09 18:49
 */
@Slf4j
@ComponentScan("com.joe.web")
public class App {
    public static void main(String[] args) throws Exception {
        SysProp prop = new SysProp();
        //开启jersey
        prop.setDisableJersey(false);
        //提供一个用户身份认证
        prop.enableAuthentication(session -> {
            User user = new User();
            user.setRoles(Collections.singleton(new Role("123", "user")));
            return user;
        });
        WebApplication.runWeb(App.class, prop, args);
    }
}
```
用过jersey的同学可以看出，上边的`JerseyApi`用的是jersey，api类修改好后启动类我们只是添加了一行`prop.setDisableJersey(false)`就开启了jersey，相比于直接使用spring-boot集成jersey，这样无疑更快了。而下边的身份认证则是本框架集合jersey提供的一个权限验证上下文，如果有需要权限验证只需要像例子那样提供一个权限认证上下文，然后在`JerseyApi`中添加@RolesAllowed注解即可。

可以看到上边出现了一个`SysProp`配置，该配置是本框架最重要的一个配置，用于对框架做各种配置来启用不同的功能。`SysProp`的定义如下：
```java
package com.joe.web.starter.core.prop;

import com.joe.web.starter.core.spi.SecureContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;

import java.util.Properties;

/**
 * web项目配置
 *
 * @author joe
 * @version 2018.02.02 11:13
 */
@ToString
public class SysProp {

    public static final int DEFAULT_MAX_SIZE = 512;
    /**
     * jersey的扫描，扫描jersey的组件
     */
    @Getter
    @Setter
    private String jerseyScan = "com.joe";
    /**
     * 项目根路径，null、""、"/"视作等效，即项目根路径都是/
     */
    @Getter
    @Setter
    private String root = "/";
    /**
     * 是否禁用jersey（禁用jersey后使用springMVC），true表示禁用，默认禁用，如果启用jersey请添加spring-boot-starter-jersey依赖
     */
    @Getter
    @Setter
    private boolean disableJersey = true;
    /**
     * 是否允许跨域，默认不允许，只有在disableJersey等于false时生效
     */
    @Getter
    @Setter
    private boolean enableCors = false;
    /**
     * 跨域配置，只有在enableCors为true时才生效
     */
    @Getter
    @Setter
    private String allowOrigin = "*";
    /**
     * 跨域配置，只有在enableCors为true时才生效
     */
    @Getter
    @Setter
    private String allowMethods = "POST, GET, OPTIONS, DELETE";
    /**
     * 跨域配置，只有在enableCors为true时才生效
     */
    @Getter
    @Setter
    private String maxAge = "3600";
    /**
     * 跨域配置，只有在enableCors为true时才生效
     */
    @Getter
    @Setter
    private String allowHeaders = "Accept, Origin, XRequestedWith, Content-Type, LastModified";
    /**
     * 开启流量统计时最多打印多少byte内容，当小于等于0时使用默认值{@link #DEFAULT_MAX_SIZE}
     */
    @Getter
    @Setter
    private int maxReadSize = DEFAULT_MAX_SIZE;
    /**
     * 是否禁用异常屏蔽，true表示禁用，如果禁用那么系统的异常信息将发送到前台
     */
    @Getter
    @Setter
    private boolean disableExceptionMapper = false;
    /**
     * 嵌入式web容器
     */
    @Getter
    @Setter
    private ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer;
    /**
     * 安全上下文，如果要启动权限拦截那么需要通过enableAuthentication方法设置该值，并且需要启用jersey
     */
    @Getter
    private SecureContext secureContext;
    /**
     * spring系统配置
     */
    private final Properties properties;


    public SysProp() {
        this.properties = new Properties();
    }

    /**
     * 增加配置（该配置会被application.properties中的配置覆盖）
     *
     * @param key   spring配置的key
     * @param value spring配置的value
     */
    public void addProperties(String key, Object value) {
        properties.put(key, value);
    }

    /**
     * 获取当前spring配置（clone出来的副本）
     *
     * @return 当前spring配置
     */
    public Properties getProperties() {
        return (Properties) this.properties.clone();
    }

    /**
     * 设置系统监听端口号（该端口号会被application.properties中的配置覆盖）
     *
     * @param port 要设置的端口号
     */
    public void setPort(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException("端口号不能小于等于0");
        }
        properties.put("server.port", port);
    }

    /**
     * 设置允许动态动态加载JSP,开启动态加载时JSP更改后服务器不用重启即可看到更改内容，但是会带来性能问题，导致性能下
     * 降，生产环境不建议开启，默认关闭
     */
    public void allowJspReload() {
        properties.put("server.jsp-servlet.init-parameters.development", true);
    }

    /**
     * 启用权限拦截
     *
     * @param context 安全上下文，用户自己实现
     */
    public void enableAuthentication(SecureContext context) {
        if (context == null) {
            throw new NullPointerException("安全上下文不能为null");
        }
        this.secureContext = context;
    }
}
```
对于各个配置项都有详细的注解，可以看出很多较复杂功能例如启用jersey、允许跨域等只需要一个配置即可使用，同时由于框架只是对spring-boot进行了超简单的封装，所以丝毫不会影响效率。

注意：如果使用jersey功能需要在pom中加入如下依赖：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jersey</artifactId>
    <scope>provided</scope>
</dependency>
```
## 支付能力
本框架提供了支付宝/微信支付能力，用户在使用本框架的时候不用关系底层如何调用，只需要使用上层统一API即可完成支付，支付接口如下：
```java
package com.joe.pay;

import com.joe.pay.pojo.PayRequest;
import com.joe.pay.pojo.prop.PayProp;
import com.joe.pay.pojo.PayResponse;

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
     * @param param 支付参数
     * @return 支付结果
     */
    PayResponse pay(PayParam param);
}
```
使用示例如下：
```java
package com.joe.pay;

import com.joe.pay.pojo.PayRequest;
import com.joe.pay.pojo.prop.PayProp;
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
        wxProp = PayProp.builder()
                .appid("123123123")
                .key("123123123")
                .mchId("123123123")
                .notifyUrl("http://www.baidu.com")
                .mode(PayProp.PayMode.WECHAT)
                .build();
        wxPayService = PayServiceFactory.getInstance(wxProp);

        String aliKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDEuQvaAad8+PAUVLeh9tqNsWNDCiAXEal4krM0E1" +
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
                "W0JukNWDL2tOzTSZ5ABAwqorV3bM67mLUSt0O5dL4YtSBJ2J3F6joO1fTZEcUCybO/A5J4wTZDOV1AR";
        //配置支付宝支付，需要将下列参数替换为自己的参数
        aliProp = PayProp.builder()
                .appid("123")
                .key(aliKey)
                .notifyUrl("http://www.baidu.com")
                .mode(PayProp.PayMode.ALIAPP)
                .build();
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
        PayParam payRequest = new PayParam();
        payRequest.setOutTradeNo(Tools.createUUID());
        payRequest.setBody("天天爱消除-游戏充值");
        payRequest.setSubject("天天爱消除-游戏充值");
        payRequest.setCreateTime(DateUtil.getFormatDate(DateUtil.BASE));
        payRequest.setTotalAmount(100 * 10);
        payRequest.setIp("106.120.141.226");
        return payRequest;
    }
}
```
可以看出，对于微信支付和支付宝支付调用方式是一模一样的，不同的是只需要创建不同的`PayService`实例即可。而如果`PayService`提供的pay接口的参数不满足您的业务，而您对支付宝和微信支付的底层业务有比较熟悉，那么本框架还给您提供了另一种解决方案，`AliPayService`和`WxPayService`都提供了一个重载pay方法（需要将`PayService`强转为`AliPayService`或者`WxPayService`），只需要使用该重载方法即可使用完整的功能，一般来说PayService接口提供的pay功能即可满足大多数支付需求。

注：当前暂时没有退款等相关接口，该部分接口正在开发中。