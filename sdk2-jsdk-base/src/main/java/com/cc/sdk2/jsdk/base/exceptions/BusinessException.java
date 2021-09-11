package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 * 业务异常
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 **/
public final class BusinessException extends BaseCheckedException {

    public BusinessException(int groupId, int serviceId) {
        super(groupId, serviceId, 560, "Business Error");
    }


    public BusinessException(int groupId, int serviceId, String defaultMessage) {
        super(groupId, serviceId, 560, defaultMessage);
    }

    public BusinessException(int groupId, int serviceId, String defaultMessage, String i18nCode) {
        super(groupId, serviceId, 560, defaultMessage, i18nCode);
    }

    public BusinessException(int groupId, int serviceId, String defaultMessage, String i18nCode, Object[] args) {
        super(groupId, serviceId, 560, defaultMessage, i18nCode, args);
    }
    public BusinessException(int groupId, int serviceId, String defaultMessage, String i18nCode, Throwable e) {
        super(groupId, serviceId, 560, defaultMessage, i18nCode, e);
    }

}
