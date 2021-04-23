package com.joe.web.starter.core.model.dto;

import lombok.Data;

/**
 * 接口调用信息
 *
 * @author joe
 */
@Data
public class InterfaceInfo {

    /**
     * 耗时，单位毫秒
     */
    private int     consumeTime;

    /**
     * 开始时间，格式yyyy-MM-dd HH:mm:ss SSS
     */
    private String  beginTime;

    /**
     * 结束时间，格式yyyy-MM-dd HH:mm:ss SSS
     */
    private String  endTime;

    /**
     * 用户实际请求路径，除去根路径的全部名称，如果包含PathParam则填写实际的值而不是PathParam的表达式，例如delete/qiao
     */
    private String  realRequestAddr;

    /**
     * 用户请求的完整路径
     */
    private String  path;

    /**
     * 请求方法，POST、GET、DELETE、PUT等
     */
    private String  method;

    /**
     * 请求方IP
     */
    private String  ip;

    /**
     * 是否完成,true：完成，false：未完成
     */
    private boolean finish = false;
}
