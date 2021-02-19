package com.cc.sdk2.jsdk.commons.localcache.caches;

import com.cc.sdk2.jsdk.commons.localcache.*;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventType;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class EhcacheLocalCache<K, V> extends AbstractCache<K, V> {
    private Cache<K, V> ehcache;
    private final Class<K> keyType;
    private final Class<V> valueType;

    protected EhcacheLocalCache(String cacheName, Class<K> keyType, Class<V> valueType, long expiredTime, TimeUnit timeUnit, CacheLoader<K, V> myCacheLoader, Set<CacheListener<K, V>> myCacheListeners) {
        super(cacheName, expiredTime, timeUnit, myCacheLoader, myCacheListeners);
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    protected void init(Object realCacheBuilder) {
        CacheConfigurationBuilder<K, V> configBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder(keyType, valueType, ResourcePoolsBuilder.heap(Integer.MAX_VALUE));
        if (expiredTime > 0) {
            Duration expireDuration;
            switch (timeUnit) {
                case SECONDS:
                    expireDuration = Duration.ofSeconds(expiredTime);
                    break;
                case MINUTES:
                    expireDuration = Duration.ofMinutes(expiredTime);
                    break;
                case HOURS:
                    expireDuration = Duration.ofHours(expiredTime);
                    break;
                default:
                    expireDuration = Duration.ofSeconds(expiredTime);
                    break;
            }
            configBuilder = configBuilder.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(expireDuration));
        }
        if (myCacheListeners != null && myCacheListeners.size() > 0) {
            CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
                    .newEventListenerConfiguration((CacheEventListener<K, V>) event -> {
                        EventType type = event.getType();
                        switch (type) {
                            case CREATED:
                                myCacheListeners.forEach(item -> item.onCreate(event.getKey(), event.getOldValue()));
                                break;
                            case UPDATED:
                                myCacheListeners.forEach(item -> item.onUpdate(event.getKey(), event.getOldValue()));
                                break;
                            case REMOVED:
                                myCacheListeners.forEach(item -> item.onRemove(event.getKey(), event.getOldValue()));
                                break;
                            case EXPIRED:
                                System.out.println(event.getKey() + "expired");
                                break;
                            case EVICTED:
                                System.out.println(event.getKey() + "evicted");
                                break;
                            default:
                                break;
                        }
                    }, EventType.CREATED, EventType.UPDATED, EventType.REMOVED)
                    .unordered().asynchronous();
            configBuilder = configBuilder.withService(cacheEventListenerConfiguration);
        }
        if (myCacheLoader != null) {
            configBuilder = configBuilder.withLoaderWriter(new CacheLoaderWriter<K, V>() {
                @Override
                public V load(K key) throws Exception {
                    return myCacheLoader.load(key);
                }

                @Override
                public Map<K, V> loadAll(Iterable<? extends K> keys) throws BulkCacheLoadingException, Exception {
                    Set<K> keySet = new HashSet<>();
                    keys.forEach(item -> keySet.add(item));
                    return myCacheLoader.loadAll(keySet);
                }

                @Override
                public void write(K key, V value) throws Exception {

                }

                @Override
                public void delete(K key) throws Exception {

                }
            });
        }
        CacheManager cacheManager = (CacheManager) realCacheBuilder;
        this.ehcache = cacheManager.createCache(this.cacheName, configBuilder.build());
    }

    @Override
    public String getCacheName() {
        return super.cacheName;
    }

    @Override
    public V get(K key) {
        requestTimes++;
        try {
            V value = ehcache.get(key);
            if (value == null) {
                missedTimes++;
            }
            return ehcache.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        missedTimes++;
        return null;
    }

    @Override
    public Map<K, V> getAll(Collection<? extends K> keys) {
        Set<K> keySets = new HashSet<>();
        keySets.addAll(keys);
        return ehcache.getAll(keySets);
    }

    @Override
    public void put(K key, V value) {
        ehcache.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> kvMap) {
        ehcache.putAll(kvMap);
    }

    @Override
    public void remove(K key) {
        ehcache.remove(key);
    }
}
