package com.cc.sdk2.jsdk.base.exceptions;

/**
 * unauthorized exception
 *
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 */
public class UnauthorizedException extends BaseException {


    public UnauthorizedException(String msgCode) {
        super(msgCode);
    }

    public UnauthorizedException(String message, String msgCode) {
        super(message, msgCode);
    }
}
