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
import com.joe.web.starter.core.model.secure.Role;
import com.joe.web.starter.core.model.secure.User;
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

[微信支付/支付宝支付能力](pay.md)