package com.joe.web.starter.core.spring.secure;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.joe.web.starter.core.exception.ErrorCode;
import com.joe.web.starter.core.exception.ServerException;
import com.joe.web.starter.core.model.secure.User;
import com.joe.web.starter.core.spi.SecureContext;

/**
 * @author JoeKerouac
 * @data 2021-04-09 19:13
 */
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecureContext secureContext;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用csrf功能
        http.csrf().disable();
        // 用于从请求中构建认证信息，但是不对信息进行认证
        http.addFilterBefore(new AbstractAuthenticationProcessingFilter("/**") {

            @Override
            public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                throws AuthenticationException, IOException, ServletException {
                User user = Optional.ofNullable(secureContext.getUser(request)).orElse(User.createAnonymous());
                return new WebxAuthentication(user);
            }

            @Override
            protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                AuthenticationException failed) throws IOException, ServletException {
                throw new ServerException(ErrorCode.NO_AUTHENTICATION, "认证异常", failed);
            }

            @Override
            protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                FilterChain chain, Authentication authResult) throws IOException, ServletException {
                SecurityContextHolder.getContext().setAuthentication(authResult);
                // 身份验证成功就继续下一个
                chain.doFilter(request, response);
            }
        }, ExceptionTranslationFilter.class);

        super.configure(http);
    }

}
