package com.cc.sdk2.redis;


import com.cc.sdk2.jsdk.commons.distribute.DistLokException;
import com.cc.sdk2.jsdk.commons.distribute.DistributedLock;
import com.cc.sdk2.redis.clients.RedisClient;

/**
 * redis实现分布式锁
 */
public class RedisDistributedLock implements DistributedLock {

    private final RedisClient redisClient;

    public RedisDistributedLock(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public void tryLock(String requestId, String methodName, int waitTime, int lockTime) throws DistLokException {
        long startWait = System.currentTimeMillis();
        synchronized (RedisDistributedLock.class) {
            do {
                long i = redisClient.setnx("mylock:" + methodName, requestId);
                if (i == 1) {
                    redisClient.expire("mylock:" + methodName, lockTime);
                    return;
                }
            } while ((System.currentTimeMillis() - startWait) / 1000 < waitTime);
            throw new DistLokException(methodName + "未能获得锁", requestId, methodName);
        }
    }

    @Override
    public void release(String requestId, String methodName) {
        String lockValue = redisClient.get("mylock:" + methodName);
        if (lockValue.equals(requestId)) {
            redisClient.del("mylock:" + methodName);
        }
    }
}
