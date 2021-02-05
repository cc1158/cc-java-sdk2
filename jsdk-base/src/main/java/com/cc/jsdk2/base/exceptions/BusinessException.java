package com.cc.jsdk2.base.exceptions;

import java.util.Locale;

/**
 * All rights reserved, copyright@cc.hu
 * 业务异常
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 **/
public final class BusinessException extends BaseException {

    /**
     * msg中的参数
     */
    private Object[] args;
    /**
     * 默认msg
     */
    private String defaultMsg;
    /**
     * local
     */
    private Locale locale;

    public BusinessException(String msgCode, Object[] args) {
        super(msgCode);
        this.args = args;
    }

    public BusinessException(String msgCode, Object[] args, String defaultMsg) {
        super(defaultMsg, msgCode);
        this.args = args;
        this.defaultMsg = defaultMsg;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getDefaultMsg() {
        return defaultMsg;
    }

    public void setDefaultMsg(String defaultMsg) {
        this.defaultMsg = defaultMsg;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
