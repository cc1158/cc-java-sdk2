package com.cc.sdk2.jsdk.base.exceptions;

import java.util.Locale;

/**
 * All rights reserved, copyright@cc.hu
 * 业务异常
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 **/
public final class BusinessException extends BaseException {

    public BusinessException(String msgCode) {
        super(msgCode);
    }

    public BusinessException(String msgCode, String defaultMsg) {
        super(msgCode, defaultMsg);
    }

    public BusinessException(String msgCode, String defaultMsg, Object[] args) {
        super(msgCode, defaultMsg, args);
    }

    public BusinessException(String msgCode, String defaultMsg, Object[] args, Throwable cause) {
        super(msgCode, defaultMsg, args, cause);
    }

}
