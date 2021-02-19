package com.cc.sdk2.jsdk.commons.localcache.caches;

import com.cc.sdk2.jsdk.base.SimpleThreadFactory;
import com.cc.sdk2.jsdk.commons.localcache.*;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2019/11/14 21:06
 **/
public final class CacheBuilder<K, V> {

    /**
     * cache map
     */
    static final Map<String, Cache<?, ?>> CACHE_MAP = new TreeMap<>();

    private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1, new SimpleThreadFactory("CacheMonitor", true));

    static {
        scheduler.scheduleWithFixedDelay(new CacheMonitor(), 1, 60, TimeUnit.SECONDS);
    }

    private static final Map<String, CacheManager> EHCACHE_CACHE_MANAGER_MAP = new HashMap<>();

    private static final CacheManager EHCACHE_MANAGER = CacheManagerBuilder.newCacheManagerBuilder().build(true);



    private String cacheName;
    private long expiredTime = 30;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private CacheLoader<K, V> loader;
    private Set<CacheListener<K, V>> listenerSet = new HashSet<>();


    public CacheBuilder cacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public CacheBuilder expiredTime(long expiredTime) {
        this.expiredTime = expiredTime <= 0 ? 30 : expiredTime;
        return this;
    }

    public CacheBuilder timeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public CacheBuilder cacheLoader(CacheLoader cacheLoader) {
        this.loader = cacheLoader;
        return this;
    }

    public CacheBuilder addCacheListener(CacheListener<K, V> cacheListener) {
        listenerSet.add(cacheListener);
        return this;
    }

    public Cache<K, V> buildGuava() {
        if (this.cacheName == null || "".equals(this.cacheName.trim())) {
            this.cacheName = UUID.randomUUID().toString();
        }
        GuavaLocalCache<K, V> cache = new GuavaLocalCache<K, V>(this.cacheName, this.expiredTime, this.timeUnit, this.loader, this.listenerSet);
        CACHE_MAP.put(this.cacheName, cache);
        return cache;
    }

    public Cache<K, V> buildEhcache(Class<K> keyType, Class<V> valueType) {
        if (this.cacheName == null || "".equals(this.cacheName.trim())) {
            this.cacheName = UUID.randomUUID().toString();
        }
        EhcacheLocalCache<K, V> cache = new EhcacheLocalCache<>(this.cacheName, keyType, valueType, this.expiredTime, this.timeUnit, this.loader, this.listenerSet);
        cache.init(EHCACHE_MANAGER);
        CACHE_MAP.put(this.cacheName, cache);
        return cache;
    }


}
