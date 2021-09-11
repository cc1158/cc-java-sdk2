package com.cc.sdk2.jsdk.base.exceptions;

/**
 * 未知的服务器异常
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 */
public class UnknownServerException extends BaseCheckedException {
    public UnknownServerException() {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 500, "Unknown Server Error");
    }


    public UnknownServerException(String defaultMessage) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 500, defaultMessage);
    }

    public UnknownServerException(String defaultMessage, String i18nCode) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 500, defaultMessage, i18nCode);
    }

    public UnknownServerException(String defaultMessage, String i18nCode, Object[] args) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 500, defaultMessage, i18nCode, args);
    }


    public UnknownServerException(String defaultMessage, String i18nCode, Throwable e) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 500, defaultMessage, i18nCode, e);
    }
}
