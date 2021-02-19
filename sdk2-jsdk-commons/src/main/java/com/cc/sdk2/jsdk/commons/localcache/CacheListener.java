package com.cc.sdk2.jsdk.commons.localcache;

public interface CacheListener<K, V> {

    void onCreate(K key, V v);

    void onUpdate(K key, V v);

    void onRemove(K key, V v);

}
