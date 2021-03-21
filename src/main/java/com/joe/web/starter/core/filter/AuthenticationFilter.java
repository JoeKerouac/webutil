package com.joe.web.starter.core.filter;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;

import com.joe.web.starter.core.prop.SysProp;
import com.joe.web.starter.core.secure.AppSecurityContext;
import com.joe.web.starter.core.spi.SecureContext;

import lombok.extern.slf4j.Slf4j;

/**
 * 权限验证拦截器（用户自己实现SecureContext）
 *
 * @author joe
 * @version 2018.02.02 14:59
 */
@Slf4j
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;

    @Autowired
    private SysProp            prop;

    private SecureContext      context;

    @PostConstruct
    public void init() {
        log.info("初始化权限验证拦截器");
        this.context = prop.getSecureContext();
        log.info("权限验证拦截器初始化完毕");
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (this.context != null) {
            HttpSession session = request.getSession();
            AppSecurityContext securityContext = new AppSecurityContext(context.getUser(session));
            requestContext.setSecurityContext(securityContext);
        }
    }
}
