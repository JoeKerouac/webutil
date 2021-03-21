package com.joe.web.starter.core.spi;

import javax.servlet.http.HttpSession;

import com.joe.web.starter.core.secure.entity.User;

/**
 * 安全上下文，启用权限拦截需要（使用@RolesAllowed注解）
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
     * @param session
     *            HttpSession
     * @return 权限对象
     */
    User getUser(HttpSession session);
}
