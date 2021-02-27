package com.cc.sdk2.jsdk.base.exceptions;

/**
 * Description
 * 请求参数异常
 * @author sen.hu@zhaopin.com
 * @date 18:16 2021/2/18
 */
public class ParameterException extends BaseException {

    public ParameterException(String msgCode) {
        super(msgCode);
    }

    public ParameterException(String msgCode, String defaultMsg) {
        super(msgCode, defaultMsg);
    }

    public ParameterException(String msgCode, String defaultMsg, Object[] args) {
        super(msgCode, defaultMsg, args);
    }

    public ParameterException(String msgCode, String defaultMsg, Object[] args, Throwable cause) {
        super(msgCode, defaultMsg, args, cause);
    }
}
