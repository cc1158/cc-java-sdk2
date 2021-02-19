package com.cc.sdk2.jsdk.commons.localcache;

import java.util.Collection;
import java.util.Map;

public interface CacheLoader<K, V> {
    /**
     * load value by key
     * @param key key
     * @return
     */
    V load(K key);

    /**
     * load values by keys
     * @param keys key collection
     * @return
     */
    Map<K, V> loadAll(Collection<K> keys);


}
