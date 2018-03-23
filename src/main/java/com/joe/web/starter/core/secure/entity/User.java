package com.joe.web.starter.core.secure.entity;

import lombok.Data;

import java.util.Set;

@Data
public class User {
    public static final String UNDEFINED_USER = "undefined";
    /**
     * 角色ID
     */
    private String id;
    /**
     * 角色名
     */
    private String name;
    /**
     * 对应的权限集合
     */
    private Set<Role> roles;
    /**
     * 加入的所有组的集合
     */
    private Set<UserGroup> groups;

    /**
     * 创建一个空对象
     *
     * @return
     */
    public static User createEmpty() {
        User user = new User();
        user.setName(UNDEFINED_USER);
        user.setId(UNDEFINED_USER);
        return user;
    }
}
