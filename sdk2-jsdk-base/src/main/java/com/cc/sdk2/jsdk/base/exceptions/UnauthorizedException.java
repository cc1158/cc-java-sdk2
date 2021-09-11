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
        super(1, 1, 401, "Not Authorized");
    }


    public UnauthorizedException(String defaultMessage) {
        super(1, 1, 401, defaultMessage);
    }

    public UnauthorizedException(String defaultMessage, String i18nCode) {
        super(1, 1, 401, defaultMessage, i18nCode);
    }


    public UnauthorizedException(String defaultMessage, String i18nCode, Throwable e) {
        super(1, 1, 401, defaultMessage, i18nCode, e);
    }

}
