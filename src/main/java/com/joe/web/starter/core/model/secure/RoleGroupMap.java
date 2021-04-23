package com.joe.web.starter.core.model.secure;

import lombok.Data;

/**
 * 权限-用户组的映射
 *
 * @author joe
 * @version 2018.02.02 14:32
 */
@Data
public class RoleGroupMap {

    /**
     * 映射ID
     */
    private String id;

    /**
     * 权限ID
     */
    private String rid;

    /**
     * 组ID
     */
    private String gid;

}
