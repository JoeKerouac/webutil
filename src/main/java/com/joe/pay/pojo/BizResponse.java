package com.joe.pay.pojo;

import lombok.Data;

/**
 * 业务响应
 *
 * @author joe
 * @version 2018.07.06 10:52
 */
@Data
public class BizResponse implements Response {
    /**
     * 业务是否成功
     */
    private boolean success;
    /**
     * 响应编码（失败的时候可以用）
     */
    private String  code;
    /**
     * 响应编码描述信息（失败的时候可以用）
     */
    private String  errMsg;
}
