package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 * token过期异常
 * @author cc
 * @version 1.0
 * @date 2021/2/8 13:37
 **/
public class TokenExpiredException extends BaseException{

    public TokenExpiredException(String msgCode) {
        super(msgCode);
    }

    public TokenExpiredException(String message, String msgCode) {
        super(message, msgCode);
    }
}
