package com.cc.sdk2.jsdk.commons.localcache.caches;

import com.cc.sdk2.jsdk.commons.localcache.CacheListener;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GuavaLocalCache<K, V> extends AbstractCache<K, V> {
    private LoadingCache<K, V> loadingCache;

    GuavaLocalCache(String cacheName, long expiredTime, TimeUnit timeUnit, com.cc.sdk2.jsdk.commons.localcache.CacheLoader myLoader, Set<CacheListener<K, V>> myListeners) {
        super(cacheName, expiredTime, timeUnit, myLoader, myListeners);
    }

    @Override
    protected void init(Object realBuilder) {
        loadingCache = CacheBuilder.newBuilder()
                .softValues()
                .expireAfterAccess(expiredTime, timeUnit)
                .removalListener((RemovalListener<K, V>) notification -> {
                    if (myCacheListeners != null && myCacheListeners.size() > 0) {
                        myCacheListeners.forEach(item -> item.onRemove(notification.getKey(), notification.getValue()));
                    }
                }).build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K key) throws Exception {
                        if (myCacheLoader != null) {
                            return myCacheLoader.load(key);
                        }
                        return null;
                    }
                });

    }

    @Override
    public String getCacheName() {
        return this.cacheName;
    }

    @Override
    public V get(K key) {
        requestTimes++;
        try {
            return loadingCache.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        missedTimes++;
        return null;
    }

    @Override
    public Map<K, V> getAll(Collection<? extends K> keys) {
        return null;
    }

    @Override
    public void put(K key, V value) {
        loadingCache.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> kvMap) {
        loadingCache.putAll(kvMap);
    }

    @Override
    public void remove(K key) {
        loadingCache.invalidate(key);
    }

}
