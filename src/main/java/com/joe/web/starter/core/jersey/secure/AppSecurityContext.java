package com.joe.web.starter.core.jersey.secure;

import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Set;

import org.glassfish.jersey.server.SubjectSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joe.web.starter.core.model.secure.Role;
import com.joe.web.starter.core.model.secure.User;
import com.joe.web.starter.core.model.secure.UserGroup;

/**
 * 权限上下文
 *
 * @author joe
 * @version 2018.02.02 14:12
 */
public class AppSecurityContext implements SubjectSecurityContext {
    private static final Logger logger = LoggerFactory.getLogger("Authorization");
    private User                user;
    private AppPrincipal        principal;

    public AppSecurityContext(User user) {
        setUser(user);
    }

    public void setUser(User user) {
        if (user == null) {
            user = User.createAnonymous();
        }
        if (this.principal == null) {
            this.principal = new AppPrincipal(user);
        }
        this.principal.setUser(user);
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {
        return this.principal;
    }

    /**
     * 验证权限
     */
    @Override
    public boolean isUserInRole(String role) {
        //        ForbiddenException
        //        MappableException
        logger.info("开始验证权限，需要的权限为：{}", role);
        if (user == null || User.ANONYMOUS_USER.equals(user.getName())) {
            logger.error("角色未定义");
            return false;
        }
        Set<Role> userRoles = user.getRoles();

        if (checkRole(userRoles, role)) {
            logger.info("角色{}权限验证通过", user.getName());
            return true;
        }
        logger.warn("角色{}权限验证不通过，继续验证该角色的群组权限", user.getName());
        Set<UserGroup> groups = user.getGroups();

        if (groups == null || groups.isEmpty()) {
            logger.warn("角色{}群组为空，验证失败", user.getName());
            logger.error("角色{}没有权限", user.getName());
            return false;
        }

        for (UserGroup group : user.getGroups()) {
            Set<Role> groupRole = group.getRoles();
            if (checkRole(groupRole, role)) {
                logger.info("角色{}权限验证通过", user.getName());
                return true;
            }
        }
        logger.warn("角色{}没有权限", user.getName());
        return false;
    }

    @Override
    public boolean isSecure() {
        logger.info("该请求不是HTTPS请求");
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        logger.warn("AuthenticationScheme未定义");
        return null;
    }

    @Override
    public Object doAsSubject(@SuppressWarnings("rawtypes") PrivilegedAction action) {
        return action.run();
    }

    /**
     * 验证权限列表是否包含目标权限
     *
     * @param roles      权限列表
     * @param targetRole 目标权限
     * @return <li>true：权限列表包含目标权限</li>
     * <li>false：权限列表不包含目标权限</li>
     */
    private boolean checkRole(Set<Role> roles, String targetRole) {
        if (roles == null || roles.isEmpty()) {
            logger.info("权限列表为空");
            return false;
        }
        for (Role role : roles) {
            if (targetRole.equalsIgnoreCase(role.getRole())) {
                logger.info("该角色" + user.getName() + "权限验证通过");
                return true;
            }
        }
        return false;
    }
}
