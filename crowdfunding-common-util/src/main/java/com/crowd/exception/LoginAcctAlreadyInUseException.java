package com.crowd.exception;

/**
 * 保存用户信息时，登录账号已存在抛出的异常
 *
 * @author wbxing
 */
public class LoginAcctAlreadyInUseException extends RuntimeException {

    private static final long serialVersionUID = 242981915656212L;

    public LoginAcctAlreadyInUseException() {
        super();
    }

    public LoginAcctAlreadyInUseException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public LoginAcctAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAlreadyInUseException(String message) {
        super(message);
    }

    public LoginAcctAlreadyInUseException(Throwable cause) {
        super(cause);
    }
}
