package com.cc.sdk2.redis.serialize;

/**
 * 数组反序列化
 * @author cc
 */
@FunctionalInterface
public interface BytesDeserializer<R> {

    /**
     * deserialize to obj
     * @param value  the redis value
     * @return 序列化后的类型
     */
    R apply(byte[] value);

}
