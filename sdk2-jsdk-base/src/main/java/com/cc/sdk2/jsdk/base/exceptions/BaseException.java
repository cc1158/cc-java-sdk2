package com.cc.sdk2.jsdk.base.exceptions;

import java.util.Locale;

/**
 * All rights reserved, copyright@cc.hu
 * 业务异常
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 **/
public class BaseException extends RuntimeException {

    /**
     * 国际化msgkey
     */
    private String msgCode;
    private String defaultMsg;
    private Object[] args;
    /**
     * local
     */
    private Locale locale;

    public BaseException(String msgCode) {
       this(msgCode, null);
    }


    public BaseException(String msgCode, String defaultMsg) {
        this(msgCode, defaultMsg, null);
    }

    public BaseException(String msgCode, String defaultMsg,Object[] args) {
        this(msgCode, defaultMsg, args, null);
    }



    public BaseException(String msgCode, String defaultMsg, Object[] args, Throwable cause) {
        super(cause);
        this.msgCode = msgCode;
        this.defaultMsg = defaultMsg;
        this.args = args;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getDefaultMsg() {
        return defaultMsg;
    }

    public void setDefaultMsg(String defaultMsg) {
        this.defaultMsg = defaultMsg;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
