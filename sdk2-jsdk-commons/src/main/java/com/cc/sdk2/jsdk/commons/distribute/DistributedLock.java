package com.cc.sdk2.jsdk.commons.distribute;

/**
 * 分布式锁接口
 */
public interface DistributedLock {
    /**
     * 尝试获得锁
     * @param reqId     请求id
     * @param methodName        方法包名
     * @param waitSec          等待时间（秒）
     * @param lockSec          锁时间（秒）
     * @throws DistLokException     异常
     */
    void tryLock(String reqId, String methodName, int waitSec, int lockSec) throws DistLokException;

    /**
     * 释放锁
     * @param reqId     请求id
     * @param methodName   方法包名（全局唯一）
     */
    void release(String reqId, String methodName);

}
