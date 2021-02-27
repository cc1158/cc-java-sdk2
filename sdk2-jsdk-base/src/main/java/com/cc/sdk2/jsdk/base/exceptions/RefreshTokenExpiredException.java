package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 * refreshtoken 过期异常
 * @author cc
 * @version 1.0
 * @date 2021/2/8 13:36
 **/
public class RefreshTokenExpiredException extends BaseException {


    public RefreshTokenExpiredException(String msgCode) {
        super(msgCode);
    }

    public RefreshTokenExpiredException(String msgCode, String defaultMsg) {
        super(msgCode, defaultMsg);
    }

    public RefreshTokenExpiredException(String msgCode, String defaultMsg, Object[] args) {
        super(msgCode, defaultMsg, args);
    }

    public RefreshTokenExpiredException(String msgCode, String defaultMsg, Object[] args, Throwable cause) {
        super(msgCode, defaultMsg, args, cause);
    }
}
