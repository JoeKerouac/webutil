package com.joe.web.starter.core.config;

import com.joe.utils.common.StringUtils;
import com.joe.web.starter.core.prop.SysProp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Component;

/**
 * container配置
 *
 * @author joe
 * @version 2018.02.02 10:38
 */
@Slf4j
@Component
public class CustomizationBean implements EmbeddedServletContainerCustomizer {
    private SysProp prop;

    public CustomizationBean(SysProp prop) {
        this.prop = prop;
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        String root = prop.getRoot();
        if (!StringUtils.isEmpty(root) && !"/".equals(root)) {
            container.setContextPath(root);
        } else {
            root = "/";
        }
        log.debug("项目根路径为：[{}]，端口号为：[{}]", root);
    }
}
