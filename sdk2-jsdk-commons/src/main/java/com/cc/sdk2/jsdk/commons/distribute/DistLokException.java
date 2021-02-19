package com.cc.sdk2.jsdk.commons.distribute;

/**
 * 分布式锁抛出异常
 */
public class DistLokException extends RuntimeException {
    private final String uuid;
    private final String lockMethod;

    public DistLokException(String message, String uuid, String lockMethod) {
        super(message);
        this.uuid = uuid;
        this.lockMethod = lockMethod;
    }

    public String getUuid() {
        return uuid;
    }

    public String getLockMethod() {
        return lockMethod;
    }
}
