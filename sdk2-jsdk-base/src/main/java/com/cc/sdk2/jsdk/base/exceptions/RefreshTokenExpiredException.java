package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 * refreshtoken 过期异常
 * @author cc
 * @version 1.0
 * @date 2021/2/8 13:36
 **/
public class RefreshTokenExpiredException extends BaseCheckedException {


    public RefreshTokenExpiredException() {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 492, "Refresh Token Is Expired");
    }


    public RefreshTokenExpiredException(String defaultMessage) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 492, defaultMessage);
    }

    public RefreshTokenExpiredException(String defaultMessage, String i18nCode) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 492, defaultMessage, i18nCode);
    }

    public RefreshTokenExpiredException(String defaultMessage, String i18nCode, Object[] args) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 492, defaultMessage, i18nCode, args);
    }


    public RefreshTokenExpiredException(String defaultMessage, String i18nCode, Throwable e) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 492, defaultMessage, i18nCode, e);
    }
}
