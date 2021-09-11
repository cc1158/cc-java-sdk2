package com.cc.sdk2.jsdk.base.exceptions;

/**
 * unauthorized exception
 *
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 */
public class UnauthorizedException extends BaseCheckedException {

    public UnauthorizedException() {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 401, "Not Authorized");
    }


    public UnauthorizedException(String defaultMessage) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 401, defaultMessage);
    }

    public UnauthorizedException(String defaultMessage, String i18nCode) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 401, defaultMessage, i18nCode);
    }

    public UnauthorizedException(String defaultMessage, String i18nCode, Object[] args) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 401, defaultMessage, i18nCode, args);
    }


    public UnauthorizedException(String defaultMessage, String i18nCode, Throwable e) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 401, defaultMessage, i18nCode, e);
    }

}
