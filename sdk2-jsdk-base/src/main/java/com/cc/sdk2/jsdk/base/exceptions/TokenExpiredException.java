package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 * token过期异常
 * @author cc
 * @version 1.0
 * @date 2021/2/8 13:37
 **/
public class TokenExpiredException extends BaseCheckedException {


    public TokenExpiredException() {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 491, "Token Is Expired");
    }


    public TokenExpiredException(String defaultMessage) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 491, defaultMessage);
    }

    public TokenExpiredException(String defaultMessage, String i18nCode) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 491, defaultMessage, i18nCode);
    }

    public TokenExpiredException(String defaultMessage, String i18nCode, Object[] args) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 491, defaultMessage, i18nCode, args);
    }


    public TokenExpiredException(String defaultMessage, String i18nCode, Throwable e) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 491, defaultMessage, i18nCode, e);
    }
}
