package com.joe.pay.exception;

/**
 * 校验签名异常
 *
 * @author joe
 * @version 2018.07.02 11:07
 */
public class CheckSignException extends PayException {
    public CheckSignException() {
        super();
    }

    public CheckSignException(String message) {
        super(message);
    }

    public CheckSignException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckSignException(Throwable cause) {
        super(cause);
    }

    protected CheckSignException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
