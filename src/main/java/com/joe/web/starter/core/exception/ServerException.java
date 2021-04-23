package com.joe.web.starter.core.exception;

import javax.validation.constraints.NotBlank;

import com.joe.utils.common.Assert;

import lombok.Getter;

/**
 * 服务端异常
 *
 * @author JoeKerouac
 * @data 2021-04-09 19:33
 */
public class ServerException extends RuntimeException {

    /**
     * 错误码
     */
    @Getter
    private final String code;

    /**
     * 错误消息，前台展示用
     */
    @Getter
    private final String msg;

    public ServerException(@NotBlank String code, @NotBlank String msg) {
        this(code, msg, null);

    }

    public ServerException(@NotBlank String code, @NotBlank String msg, Throwable cause) {
        super(msg, cause);
        Assert.notBlank(code, "错误码不能为空");
        Assert.notBlank(msg, "错误消息不能为空");
        this.code = code;
        this.msg = msg;
    }
}
