package com.joe.pay.exception;

/**
 * 支付顶级异常
 *
 * @author joe
 * @version 2018.07.02 11:06
 */
public class PayException extends RuntimeException {
    public PayException() {
        super();
    }

    public PayException(String message) {
        super(message);
    }

    public PayException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayException(Throwable cause) {
        super(cause);
    }

    protected PayException(String message, Throwable cause, boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
