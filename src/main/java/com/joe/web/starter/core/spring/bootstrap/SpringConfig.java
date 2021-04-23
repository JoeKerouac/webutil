package com.joe.web.starter.core.spring.bootstrap;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.joe.web.starter.core.condition.EnableCorsCondition;
import com.joe.web.starter.core.condition.EnableSpringMvcCondition;
import com.joe.web.starter.core.exception.AbstractExceptionMapper;
import com.joe.web.starter.core.model.secure.User;
import com.joe.web.starter.core.prop.SysProp;
import com.joe.web.starter.core.spi.ExceptionHandler;
import com.joe.web.starter.core.spi.SecureContext;
import com.joe.web.starter.core.spring.exception.SpringExceptionMapper;
import com.joe.web.starter.core.spring.secure.SpringSecurityConfig;

/**
 * @author JoeKerouac
 * @data 2021-04-10 09:18
 */
public class SpringConfig {

    @Bean
    @Conditional({EnableCorsCondition.class})
    public CorsFilter corsFilter(SysProp sysProp) {
        // 允许跨域
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(sysProp.getAllowOrigins());
        corsConfiguration.setAllowedHeaders(sysProp.getAllowHeaders());
        corsConfiguration.setAllowedMethods(sysProp.getAllowMethods());
        corsConfiguration.setMaxAge(sysProp.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有url开启
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Bean
    @Conditional({EnableSpringMvcCondition.class})
    public SpringExceptionMapper exceptionMapper() {
        return new SpringExceptionMapper();
    }

    @Bean
    @Conditional({EnableSpringMvcCondition.class})
    public SpringSecurityConfig securityConfig() {
        return new SpringSecurityConfig();
    }

    @Bean
    @ConditionalOnMissingBean(ExceptionHandler.class)
    public ExceptionHandler defaultExceptionHandler() {
        return ex -> AbstractExceptionMapper.ERROR;
    }

    @Bean
    @ConditionalOnMissingBean(SecureContext.class)
    public SecureContext defaultSecureContext() {
        return request -> User.createAnonymous();
    }

}
