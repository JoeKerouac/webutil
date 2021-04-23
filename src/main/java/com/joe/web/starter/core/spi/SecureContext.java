package com.joe.web.starter.core.spi;

import javax.servlet.http.HttpServletRequest;

import com.joe.web.starter.core.model.secure.User;

/**
 * 安全上下文，需要用户自己实现getUser逻辑
 * 
 * 如果当前spring上下文中包含该类型的bean则自动使用
 *
 * @author joe
 * @version 2018.03.19 16:10
 */
public interface SecureContext {

    /**
     * 从session中得到一个权限对象，具体怎么从里边取出根据具体实现而定
     *
     * @param request
     *            当前请求对象
     * @return 权限对象，返回null时自动使用匿名用户
     */
    User getUser(HttpServletRequest request);
}
