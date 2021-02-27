package com.cc.sdk2.jsdk.base.exceptions;

/**
 * unauthorized exception
 *
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 */
public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String msgCode) {
        super(msgCode);
    }

    public UnauthorizedException(String msgCode, String defaultMsg) {
        super(msgCode, defaultMsg);
    }

    public UnauthorizedException(String msgCode, String defaultMsg, Object[] args) {
        super(msgCode, defaultMsg, args);
    }

    public UnauthorizedException(String msgCode, String defaultMsg, Object[] args, Throwable cause) {
        super(msgCode, defaultMsg, args, cause);
    }
}
