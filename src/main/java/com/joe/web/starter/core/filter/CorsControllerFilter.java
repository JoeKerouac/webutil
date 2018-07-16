package com.joe.web.starter.core.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.joe.web.starter.core.prop.SysProp;

/**
 * 允许跨域问题，跨域控制器
 *
 * @author joe
 * @version 2018.02.09 13:39
 */
public class CorsControllerFilter implements ContainerResponseFilter {
    @Autowired
    private SysProp prop;

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.add("Access-Control-Allow-Origin", prop.getAllowOrigin());
        headers.add("Access-Control-Allow-Methods", prop.getAllowMethods());
        headers.add("Access-Control-Max-Age", prop.getMaxAge());
        headers.add("Access-Control-Allow-Headers", prop.getAllowHeaders());
    }
}
