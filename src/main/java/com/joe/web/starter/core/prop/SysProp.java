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
     * spring扫描包名
     */
    @Getter
    @Setter
    private String springScan;
    /**
     * 是否禁用jersey（禁用jersey后使用springMVC），true表示禁用
     */
    @Getter
    @Setter
    private boolean disableJersey = false;
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
    private Properties properties;


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
