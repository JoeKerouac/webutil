package com.joe.web.starter.core.jersey.bootstrap;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

import com.joe.web.starter.core.condition.EnableJerseyCondition;
import com.joe.web.starter.core.jersey.config.JerseySpringConfig;
import com.joe.web.starter.core.jersey.ext.JerseySpringBeanScannerConfigurer;

/**
 * @author JoeKerouac
 * @data 2021-04-10 10:29
 */
public class JerseyConfig {

    @Bean
    @Conditional({EnableJerseyCondition.class})
    public JerseySpringConfig jerseySpringConfig(ConfigurableApplicationContext context) {
        return new JerseySpringConfig(context);
    }

    @Bean
    @Conditional({EnableJerseyCondition.class})
    public JerseySpringBeanScannerConfigurer jerseySpringBeanScannerConfigurer() {
        return new JerseySpringBeanScannerConfigurer();
    }



}
