package com.cc.sdk2.redis.serialize;

/**
 * 字符串反序列化
 * @author Administrator
 */
@FunctionalInterface
public interface StringDeserializer<R> {
    /**
     * deserialize string to object
     * @param value the redis value
     * @return 要序列化的类型
     */
   R apply(String value);

}
