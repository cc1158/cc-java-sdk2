package com.cc.sdk2.redis.serialize;

/**
 * 数组反序列化
 * @author cc
 */
public interface BytesDeserializer {

    /**
     * deserialize to obj
     * @param value  the redis value
     * @param clazz     the fixed class
     * @param <T>  class template
     * @return
     */
    <T> T deserialize(byte[] value, Class<T> clazz);

}
