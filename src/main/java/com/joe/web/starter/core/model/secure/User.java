package com.joe.web.starter.core.model.secure;

import java.util.Set;

import lombok.Data;

@Data
public class User {

    /**
     * 匿名用户
     */
    public static final String ANONYMOUS_USER = "anonymous";

    /**
     * 当前用户详情，可能为null
     */
    private Object user;

    /**
     * 角色ID
     */
    private final String id;

    /**
     * 角色名
     */
    private final String name;

    /**
     * 对应的权限集合
     */
    private Set<Role> roles;

    /**
     * 加入的所有组的集合
     */
    private Set<UserGroup> groups;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 创建一个匿名对象
     *
     * @return 匿名对象
     */
    public static User createAnonymous() {
        return new User(ANONYMOUS_USER, ANONYMOUS_USER);
    }
}
