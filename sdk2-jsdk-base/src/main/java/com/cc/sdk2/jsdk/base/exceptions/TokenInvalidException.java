package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 * 非法token错误
 * @author cc
 * @version 1.0
 * @date 2021/2/8 13:33
 **/
public class TokenInvalidException extends BaseException {

    public TokenInvalidException(String msgCode) {
        super(msgCode);
    }

    public TokenInvalidException(String message, String msgCode) {
        super(message, msgCode);
    }
}
