package com.joe.web.starter.core.config;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

import com.joe.utils.common.string.StringUtils;
import com.joe.web.starter.core.prop.SysProp;

import lombok.extern.slf4j.Slf4j;

/**
 * container配置
 *
 * @author joe
 * @version 2018.02.02 10:38
 */
@Slf4j
@Component
public class CustomizationBean implements
                               WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    private SysProp prop;

    public CustomizationBean(SysProp prop) {
        this.prop = prop;
    }

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        String root = prop.getRoot().trim();

        if (StringUtils.isEmpty(root) || "/".equals(root)) {
            root = "/";
        }

        root = StringUtils.trim(root, "/");
        if (!root.startsWith("/")) {
            root = "/" + root;
        }

        factory.setContextPath(root);
        log.debug("项目根路径为：[{}]，端口号为：[{}]", root);
    }
}
