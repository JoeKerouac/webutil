package com.joe.web.starter;

import com.joe.utils.common.StringUtils;
import com.joe.web.starter.core.Const;
import com.joe.web.starter.core.config.JerseyConfig;
import com.joe.web.starter.core.ext.JerseySpringBeanScannerConfigurer;
import com.joe.web.starter.core.filter.CorsControllerFilter;
import com.joe.web.starter.core.filter.SystemExceptionMapper;
import com.joe.web.starter.core.prop.SysProp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private static final Class<? extends ConfigurableEmbeddedServletContainer>[] embeddedServletContainerClass = new
            Class[3];
    private static SysProp sysProp;

    static {
        embeddedServletContainerClass[0] = TomcatEmbeddedServletContainerFactory.class;
        embeddedServletContainerClass[1] = JettyEmbeddedServletContainerFactory.class;
        embeddedServletContainerClass[2] = UndertowEmbeddedServletContainerFactory.class;
    }


    /**
     * 运行web程序（依赖：需要一个名叫Statistics的logger用来打印统计信息）
     *
     * @param source 用户主类
     * @param prop   系统配置
     * @param args   参数
     * @return ConfigurableApplicationContext
     */
    public static ConfigurableApplicationContext runWeb(Object source, SysProp prop, String[] args) {
        WebApplication.sysProp = prop;

        List<Object> sources = new ArrayList<>();
        sources.add(WebApplication.class);
        if (source != null) {
            sources.add(source);
        }

        log.debug("Spring source 为：{}", sources);

        if (WebApplication.sysProp == null) {
            WebApplication.sysProp = new SysProp();
            log.warn("配置文件为null，使用默认配置文件：[{}]", WebApplication.sysProp);
        } else {
            //检查配置文件
            checkProp(WebApplication.sysProp);
        }

        if (!StringUtils.isEmpty(WebApplication.sysProp.getSpringScan())) {
            sources.add(WebApplication.sysProp.getSpringScan());
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

        SpringApplication application = new SpringApplication(sources.toArray(new Object[sources.size()]));
        //Spring配置
        application.setDefaultProperties(WebApplication.sysProp.getProperties());
        return application.run(args);
    }

    @Bean
    public SysProp sysProp() {
        return WebApplication.sysProp;
    }

    /**
     * 明确使用tomcat的嵌入式web容器
     *
     * @return 嵌入式web容器工厂
     */
    @Bean
    public EmbeddedServletContainerFactory embeddedServletContainerFactory(SysProp sysProp) {
        ConfigurableEmbeddedServletContainer factory = sysProp.getConfigurableEmbeddedServletContainer();
        if (factory == null) {
            for (Class<? extends ConfigurableEmbeddedServletContainer> clazz : embeddedServletContainerClass) {
                try {
                    factory = clazz.newInstance();
                    break;
                } catch (Throwable e) {
                }
            }
        }

        if (factory == null) {
            throw new RuntimeException("没有合适的嵌入式web容器，请添加至少一种嵌入式web容器");
        }
        //设置doc root，默认spring-boot好像找不到
        factory.setDocumentRoot(getDocRoot(sysProp.getDocRoot()));
        return (EmbeddedServletContainerFactory) factory;
    }

    /**
     * 根据用户输入doc-root查找实际的doc-root
     *
     * @param docRootStr 用户输入doc-root
     * @return doc-rootfile
     */
    private static File getDocRoot(String docRootStr) {
        String path;
        if (docRootStr.startsWith(Const.CLASSPATH_PREFIX)) {
            try {
                path = docRootStr.substring(Const.CLASSPATH_PREFIX.length());
                path = StringUtils.trim(path, "/");
                path = Thread.currentThread().getContextClassLoader().getResource(path).getFile();
            } catch (Throwable e) {
                log.warn("用户指定doc-root[{}]不存在，使用默认doc-root[{}]", docRootStr, Const.DEFAULT_DOC_ROOT);
                path = Const.DEFAULT_DOC_ROOT;
            }
        } else if (docRootStr.startsWith(Const.FILE_PREFIX)) {
            path = docRootStr.substring(Const.FILE_PREFIX.length());
        } else {
            log.warn("用户指定doc-root[{}]不符合规则，使用默认doc-root[{}]", docRootStr, Const.DEFAULT_DOC_ROOT);
            path = Const.DEFAULT_DOC_ROOT;
        }
        File docRoot = new File(path);
        if (!docRoot.exists()) {
            log.warn("用户指定doc-root[{}]不存在，使用默认doc-root[{}]", docRootStr, Const.DEFAULT_DOC_ROOT);
            docRoot = new File(Const.DEFAULT_DOC_ROOT);
        }
        return docRoot;
    }

    /**
     * 检查配置
     *
     * @param prop 系统配置
     */
    private static void checkProp(SysProp prop) {
        if (StringUtils.isEmpty(prop.getJerseyScan())) {
            log.error("jersey scan不能为空");
            throw new IllegalArgumentException("jersey scan不能为空");
        }
    }
}
