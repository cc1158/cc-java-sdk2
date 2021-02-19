package com.cc.sdk2.jsdk.commons.localcache.caches;

import com.cc.sdk2.jsdk.commons.localcache.*;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCache<K, V> implements Cache<K, V> {

    final String cacheName;
    long requestTimes = 0;
    long missedTimes = 0;
    long expiredTime;
    TimeUnit timeUnit;
    CacheLoader<K, V> myCacheLoader;
    Set<CacheListener<K, V>> myCacheListeners;


    protected AbstractCache(String cacheName) {
        this(cacheName, 30, TimeUnit.SECONDS);
    }

    protected AbstractCache(String cacheName, long expiredTime, TimeUnit timeUnit) {
        this(cacheName, expiredTime, timeUnit, null, null);
    }

    protected AbstractCache(String cacheName, long expiredTime, TimeUnit timeUnit, CacheLoader<K, V> myCacheLoader, Set<CacheListener<K, V>> myCacheListeners) {
        this.cacheName = cacheName;
        this.expiredTime = expiredTime;
        this.timeUnit = timeUnit;
        this.myCacheLoader = myCacheLoader;
        this.myCacheListeners = myCacheListeners;
    }

    /**
     * cache init
     * @param realCacheBuilder the  real cache builder
     */
    protected abstract void init(Object realCacheBuilder);

    @Override
    public String getCacheInfo() {
        long requestCount = this.requestTimes;
        long missCount = this.missedTimes;
        StringBuilder buf = new StringBuilder();
        buf.append(" hit rate: ")
                .append(requestCount > 0 ? (double) (requestCount - missCount) / (double) requestCount : 0)
                .append("[").append(requestCount - missCount).append("/").append(requestCount).append("]")
        ;
        return buf.toString();
    }
}
