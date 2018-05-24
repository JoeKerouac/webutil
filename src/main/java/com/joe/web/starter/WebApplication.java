package com.joe.web.starter;

import com.joe.utils.common.StringUtils;
import com.joe.web.starter.core.config.JerseyConfig;
import com.joe.web.starter.core.ext.JerseySpringBeanScannerConfigurer;
import com.joe.web.starter.core.filter.CorsControllerFilter;
import com.joe.web.starter.core.filter.SystemExceptionMapper;
import com.joe.web.starter.core.prop.SysProp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

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
@EnableAutoConfiguration
public class WebApplication {
    private static SysProp sysProp;

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
