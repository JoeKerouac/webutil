package com.joe.web.starter.core.exception;

import javax.validation.constraints.NotBlank;

/**
 * 安全相关异常
 *
 * @author JoeKerouac
 * @data 2021-04-09 19:42
 */
public class SecurityException extends ServerException{

    public SecurityException(@NotBlank String code, @NotBlank String msg) {
        super(code, msg);
    }

    public SecurityException(@NotBlank String code, @NotBlank String msg, Throwable cause) {
        super(code, msg, cause);
    }
}
