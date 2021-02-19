package com.cc.sdk2.redis.serialize;

/**
 * 字符串序列化
 * @author sen.hu
 */
public interface StringSerializer {
    /**
     * serial a obj to string
     * @param obj   the object
     * @return  serial string
     */
    String serialize(Object obj);

}
