package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 * 非法token错误
 * @author cc
 * @version 1.0
 * @date 2021/2/8 13:33
 **/
public class TokenInvalidException extends BaseCheckedException {


    public TokenInvalidException() {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 490, "Token Is Null Or Invalid");
    }


    public TokenInvalidException(String defaultMessage) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 490, defaultMessage);
    }

    public TokenInvalidException(String defaultMessage, String i18nCode) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 490, defaultMessage, i18nCode);
    }

    public TokenInvalidException(String defaultMessage, String i18nCode, Object[] args) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 490, defaultMessage, i18nCode, args);
    }


    public TokenInvalidException(String defaultMessage, String i18nCode, Throwable e) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 490, defaultMessage, i18nCode, e);
    }
}
