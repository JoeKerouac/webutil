package com.joe.web.starter.core.model.secure;

import java.util.Set;

import lombok.Data;

/**
 * 群组
 *
 * @author qiao9
 */
@Data
public class UserGroup {

    /**
     * 群组ID
     */
    private String    id;

    /**
     * 群组名称
     */
    private String    name;

    /**
     * 该组对应的权限集合
     */
    private Set<Role> roles;
}
