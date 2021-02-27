package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 * session过期
 * @author cc
 * @version 1.0
 * @date 2021/2/8 13:34
 **/
public class SessionExpiredException extends BaseException {


    public SessionExpiredException(String msgCode) {
        super(msgCode);
    }

    public SessionExpiredException(String msgCode, String defaultMsg) {
        super(msgCode, defaultMsg);
    }

    public SessionExpiredException(String msgCode, String defaultMsg, Object[] args) {
        super(msgCode, defaultMsg, args);
    }

    public SessionExpiredException(String msgCode, String defaultMsg, Object[] args, Throwable cause) {
        super(msgCode, defaultMsg, args, cause);
    }
}
