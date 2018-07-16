package com.joe.pay.pojo;

import java.util.function.Function;

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
    private boolean   success;
    /**
     * 异常原因
     */
    private Throwable error;
    /**
     * 实际响应数据
     */
    private T         data;

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
        response.setError(error);
        response.setSuccess(false);
        return response;
    }

    /**
     * 处理请求响应，将结果转换为另一种结果
     *
     * @param function 请求成功回调函数
     * @param <R>      实际结果类型
     * @return 实际结果
     */
    public <R extends Response> SysResponse<R> conver(Function<T, R> function) {
        if (this.isSuccess()) {
            return SysResponse.buildSuccess(function.apply(getData()));
        } else {
            return SysResponse.buildError(getError());
        }
    }
}
