package com.cc.sdk2.redis.serialize;

/**
 * 字符串反序列化
 * @author Administrator
 */
public interface StringDeserializer {
    /**
     * deserialize string to object
     * @param value the redis value
     * @param clazz the fixed class
     * @param <T> class template
     * @return
     */
   <T> T deserialize(String value, Class<T> clazz);

}
