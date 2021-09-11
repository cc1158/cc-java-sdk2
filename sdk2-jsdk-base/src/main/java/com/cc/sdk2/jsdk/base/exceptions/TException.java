package com.cc.sdk2.jsdk.base.exceptions;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2019/7/19 23:05
 **/
public class TException extends Exception {

    public TException(String message) {
        super(message);
    }

    public TException(String message, Throwable cause) {
        super(message, cause);
    }

    public TException(Throwable cause) {
        super(cause);
    }
}
