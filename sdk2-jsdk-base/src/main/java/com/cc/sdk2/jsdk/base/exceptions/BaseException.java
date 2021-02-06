package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 * 业务异常
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 **/
class BaseException extends RuntimeException {

    /**
     * 国际化msgkey
     */
    private String msgCode;

    public BaseException(String msgCode) {
        this.msgCode = msgCode;
    }

    public BaseException(String message, String msgCode) {
        super(message);
        this.msgCode = msgCode;
    }

    public BaseException(String message, Throwable cause, String msgCode) {
        super(message, cause);
        this.msgCode = msgCode;
    }

    public BaseException(Throwable cause, String msgCode) {
        super(cause);
        this.msgCode = msgCode;
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String msgCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msgCode = msgCode;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }
}
