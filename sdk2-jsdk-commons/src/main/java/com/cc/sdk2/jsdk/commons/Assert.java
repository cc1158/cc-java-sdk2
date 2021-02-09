package com.cc.sdk2.jsdk.commons;

import java.util.function.Supplier;

/**
 * 断言插入异常
 * @author sen.hu
 * @date 2018/12/10 17:09
 **/
public abstract class Assert {

    public static void notNull(String message) {
        throw new IllegalArgumentException(message);
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            String message = (messageSupplier != null ? messageSupplier.get() : null);
            throw new IllegalArgumentException(message);
        }
    }

}
