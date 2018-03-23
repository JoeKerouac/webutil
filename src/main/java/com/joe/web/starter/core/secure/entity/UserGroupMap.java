package com.joe.web.starter.core.secure.entity;

import lombok.Data;

/**
 * 用户-用户组的映射
 *
 * @author joe
 * @version 2018.02.02 14:32
 */
@Data
public class UserGroupMap {
    /**
     * 映射ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String uid;
    /**
     * 用户对应的组ID
     */
    private String gid;
}
