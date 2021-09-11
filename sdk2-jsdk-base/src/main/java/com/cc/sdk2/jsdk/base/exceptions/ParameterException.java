package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 * 业务异常
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 **/
public class ParameterException extends BaseCheckedException {

    public ParameterException() {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 400, "Parameters Error");
    }


    public ParameterException(String defaultMessage) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 400, defaultMessage);
    }

    public ParameterException(String defaultMessage, String i18nCode) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 400, defaultMessage, i18nCode);
    }

    public ParameterException(String defaultMessage, String i18nCode, Object[] args) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 400, defaultMessage, i18nCode, args);
    }


    public ParameterException(String defaultMessage, String i18nCode, Throwable e) {
        super(BaseCode.GROUP_ID_FRAMEWORK, BaseCode.SERVICE_ID_FRAMEWORK, 400, defaultMessage, i18nCode, e);
    }
}
