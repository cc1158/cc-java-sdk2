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

    public TokenInvalidException(String msgCode, String defaultMsg) {
        super(msgCode, defaultMsg);
    }

    public TokenInvalidException(String msgCode, String defaultMsg, Object[] args) {
        super(msgCode, defaultMsg, args);
    }

    public TokenInvalidException(String msgCode, String defaultMsg, Object[] args, Throwable cause) {
        super(msgCode, defaultMsg, args, cause);
    }
}
