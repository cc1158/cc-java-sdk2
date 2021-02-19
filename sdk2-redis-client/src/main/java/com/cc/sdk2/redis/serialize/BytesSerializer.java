package com.cc.sdk2.redis.serialize;

/**
 * 字节序列化
 * @author cc.sen
 */
public interface BytesSerializer {
    /**
     * serialize to bytes array
     * @param obj   the object
     * @return  the serialized bytes
     */
    byte[] serialize(Object obj);
}
