package com.joe.web.starter.core.model.secure;

import lombok.Data;

/**
 * 用户-权限映射
 *
 * @author joe
 * @version 2018.02.02 14:32
 */
@Data
public class UserRoleMap {

    /**
     * 映射ID
     */
    private String id;

    /**
     * 用户ID
     */
    private String uid;

    /**
     * 权限ID
     */
    private String rid;
}
