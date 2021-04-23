package com.joe.web.starter.core.model.secure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限对象，权限名忽略大小写
 *
 * @author qiao9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    /**
     * 权限ID
     */
    private String id;

    /**
     * 权限名
     */
    private String role;
}
