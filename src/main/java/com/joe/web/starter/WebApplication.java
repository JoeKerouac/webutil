package com.joe.web.starter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.joe.utils.ext.DocumentRootHelper;
import com.joe.web.starter.core.config.JerseyConfig;
import com.joe.web.starter.core.ext.JerseySpringBeanScannerConfigurer;
import com.joe.web.starter.core.filter.CorsControllerFilter;
import com.joe.web.starter.core.filter.SystemExceptionMapper;
import com.joe.web.starter.core.prop.SysProp;

import lombok.extern.slf4j.Slf4j;

/**
 * 启动类
 *
 * @author joe
 * @version 2018.02.02 10:33
 */
@Slf4j
@ComponentScan("com.joe.web.starter")
@Configuration
@EnableAutoConfiguration
public class WebApplication {

    /**
     * 嵌入式web容器的class集合
     */
    private static final Class<? extends ServletWebServerFactory>[] embeddedServletContainerClass;

    /**
     * 系统配置
     */
    private static SysProp sysProp;

    static {
        embeddedServletContainerClass = new Class[3];

        embeddedServletContainerClass[0] = TomcatServletWebServerFactory.class;
        embeddedServletContainerClass[1] = JettyServletWebServerFactory.class;
        embeddedServletContainerClass[2] = UndertowServletWebServerFactory.class;
    }

    /**
     * 运行web程序（依赖：需要一个名叫Statistics的logger用来打印统计信息）
     *
     * @param source
     *            用户主类
     * @return ConfigurableApplicationContext
     */
    public static ConfigurableApplicationContext runWeb(Class<?> source) {
        return runWeb(source, null, null);
    }

    /**
     * 运行web程序（依赖：需要一个名叫Statistics的logger用来打印统计信息）
     *
     * @param source
     *            用户主类
     * @param prop
     *            系统配置
     * @return ConfigurableApplicationContext
     */
    public static ConfigurableApplicationContext runWeb(Class<?> source, SysProp prop) {
        return runWeb(source, prop, null);
    }

    /**
     * 运行web程序（依赖：需要一个名叫Statistics的logger用来打印统计信息）
     *
     * @param source
     *            用户主类
     * @param args
     *            参数
     * @return ConfigurableApplicationContext
     */
    public static ConfigurableApplicationContext runWeb(Class<?> source, String[] args) {
        return runWeb(source, null, args);
    }

    /**
     * 运行web程序（依赖：需要一个名叫Statistics的logger用来打印统计信息）
     *
     * @param source
     *            用户主类
     * @param prop
     *            系统配置
     * @param args
     *            参数
     * @return ConfigurableApplicationContext
     */
    public static ConfigurableApplicationContext runWeb(Class<?> source, SysProp prop, String[] args) {
        WebApplication.sysProp = prop;

        List<Class<?>> sources = new ArrayList<>();
        sources.add(WebApplication.class);
        if (source != null) {
            sources.add(source);
        }

        if (args == null) {
            args = new String[0];
        }

        log.debug("Spring source 为：{}", sources);

        if (WebApplication.sysProp == null) {
            WebApplication.sysProp = new SysProp();
            log.warn("配置文件为null，使用默认配置文件：[{}]", WebApplication.sysProp);
        }

        if (!WebApplication.sysProp.isDisableJersey()) {
            log.info("启用jersey");
            sources.add(JerseyConfig.class);
            sources.add(JerseySpringBeanScannerConfigurer.class);

            if (WebApplication.sysProp.isEnableCors()) {
                log.debug("允许跨域，往spring注册跨域控制器bean");
                sources.add(CorsControllerFilter.class);
                JerseyConfig.registerComponent(CorsControllerFilter.class);
            }

            if (!WebApplication.sysProp.isDisableExceptionMapper()) {
                log.info("当前系统允许异常屏蔽，注册spring bean");
                sources.add(SystemExceptionMapper.class);
                JerseyConfig.registerComponent(SystemExceptionMapper.class);
            } else {
                log.warn("当前系统不允许异常屏蔽，异常信息将直接发送到前台");
            }
        } else {
            log.warn("jersey被禁用，将采用springMVC");
        }

        SpringApplication application = new SpringApplication(sources.toArray(new Class<?>[0]));
        // Spring配置
        application.setDefaultProperties(WebApplication.sysProp.getProperties());
        return application.run(args);
    }

    @Bean
    SysProp sysProp() {
        return WebApplication.sysProp;
    }

    /**
     * 明确使用tomcat的嵌入式web容器
     *
     * @return 嵌入式web容器工厂
     */
    @Bean
    ServletWebServerFactory embeddedServletContainerFactory(SysProp sysProp) {
        ServletWebServerFactory factory = sysProp.getConfigurableEmbeddedServletContainer();
        if (factory == null) {
            for (Class<? extends ServletWebServerFactory> clazz : embeddedServletContainerClass) {
                try {
                    factory = clazz.newInstance();
                    break;
                } catch (Throwable e) {
                    log.info("构建ServletWebServerFactory[{}]失败", clazz);
                }
            }
        }

        if (factory == null) {
            throw new RuntimeException("没有合适的嵌入式web容器，请添加至少一种嵌入式web容器");
        }

        if (factory instanceof ConfigurableServletWebServerFactory) {
            // 设置doc root，spring-boot只能在打包后找到，在IDE中直接运行时找不到
            ((ConfigurableServletWebServerFactory)factory).setDocumentRoot(DocumentRootHelper.getValidDocumentRoot());
        }

        return factory;
    }
}
