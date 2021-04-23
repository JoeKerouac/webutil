package com.joe.web.starter.core.jersey.secure;

import java.security.Principal;

import com.joe.web.starter.core.model.secure.User;

/**
 * 表示一个主体，可以用来表示任何实体，例如人、公司和一个登录ID
 *
 * @author joe
 * @version 2018.02.02 14:32
 */
public class AppPrincipal implements Principal {
    private User user;

    AppPrincipal(User user) {
        setUser(user);
    }

    void setUser(User user) {
        if (user == null) {
            user = User.createAnonymous();
        }
        this.user = user;
    }

    public String getName() {
        return user.getName();
    }

    public int hashCode() {
        return user.getName().hashCode();
    }

    /**
     * 判断是不是同一个角色
     *
     * @param obj 要比较的角色
     * @return 返回true表示是同一个角色
     */
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof AppPrincipal))
            return false;

        AppPrincipal principal = (AppPrincipal) obj;
        return this.user.getId().equals(principal.user.getId());
    }

    public String toString() {
        return super.toString();
    }
}
