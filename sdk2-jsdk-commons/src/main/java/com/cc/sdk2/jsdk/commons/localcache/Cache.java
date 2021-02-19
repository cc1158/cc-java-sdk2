package com.cc.sdk2.jsdk.commons.localcache;

import java.util.Collection;
import java.util.Map;

public interface Cache<K, V> {

    String getCacheName();

    /**
     * get value by key
     * @param key
     * @return
     */
    V get(K key);

    /**
     * get all values by keys
     * @param keys
     * @return
     */
    Map<K, V> getAll(Collection<? extends K> keys);

    /**
     * put value in cache
     * @param key key
     * @param value value
     */
    void put(K key, V value);

    /**
     * put all values in cache
     * @param kvMap
     */
    void putAll(Map<? extends K, ? extends V> kvMap);

    /**
     * remove from cache
     * @param key
     */
    void remove(K key);

    /**
     * get information of the cache
     * @return
     */
    String getCacheInfo();

}
