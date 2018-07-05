package com.joe.pay.pojo;

import lombok.Data;

/**
 * 系统网络请求响应
 *
 * @author joe
 * @version 2018.07.05 11:02
 */
@Data
public class SysResponse<T extends Response> {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 异常原因
     */
    private Throwable error;
    /**
     * 实际响应数据
     */
    private T data;

    /**
     * 构建成功响应
     *
     * @param data 响应数据
     * @param <R>  数据类型
     * @return 成功响应
     */
    public static <R extends Response> SysResponse<R> buildSuccess(R data) {
        SysResponse<R> response = new SysResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    /**
     * 构建系统异常响应
     *
     * @param error 系统异常
     * @param <R>   数据类型
     * @return 异常响应
     */
    public static <R extends Response> SysResponse<R> buildError(Throwable error) {
        SysResponse<R> response = new SysResponse<>();
        response.setSuccess(false);
        response.setError(error);
        return response;
    }
}
