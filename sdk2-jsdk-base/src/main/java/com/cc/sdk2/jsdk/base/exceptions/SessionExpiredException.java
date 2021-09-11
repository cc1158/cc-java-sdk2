package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 * session过期
 * @author cc
 * @version 1.0
 * @date 2021/2/8 13:34
 **/
public class SessionExpiredException extends BaseCheckedException {


    public SessionExpiredException() {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 499, "Refresh Token Is Expired");
    }


    public SessionExpiredException(String defaultMessage) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 499, defaultMessage);
    }

    public SessionExpiredException(String defaultMessage, String i18nCode) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 499, defaultMessage, i18nCode);
    }

    public SessionExpiredException(String defaultMessage, String i18nCode, Object[] args) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 499, defaultMessage, i18nCode, args);
    }


    public SessionExpiredException(String defaultMessage, String i18nCode, Throwable e) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 499, defaultMessage, i18nCode, e);
    }
}
