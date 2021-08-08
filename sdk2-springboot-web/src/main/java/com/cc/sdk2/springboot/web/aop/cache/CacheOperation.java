package com.cc.sdk2.springboot.web.aop.cache;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/8/4 21:26
 **/
public interface CacheOperation {
    /**
     * 加入缓存
     * @param key  缓存key
     * @param value   数据
     * @return  0  成功   1  失败
     */
    int put(String key, Object value);

    /**
     * 加入缓存
     * @param key  缓存key
     * @param value  数据
     * @param validTime   有效时间
     * @return  0  成功   1  失败
     */
    int put(String key, Object value, int validTime);

    /**
     * 获取缓存
     * @param key   缓存key
     * @param rClass  返回类型
     * @param <R>  返回类型
     * @return  缓存内容
     */
    <R> R get(String key, Class<R> rClass);

    /**
     * 删除缓存
     * @param key  缓存key
     * @return  0  成功   1  失败
     */
    int remove(String key);

}
