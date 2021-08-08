package com.cc.sdk2.springboot.web.aop.cache;

import java.lang.annotation.*;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/8/4 21:29
 **/
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    /**
     * 缓存key
     *
     * @return 缓存key
     */
    String key() default "";

    /**
     * 缓存有效时间  -1 一直有效
     *
     * @return 缓存有效时间
     */
    int validTime() default -1;

    /**
     * 缓存实现类
     *
     * @return 缓存实现类
     */
    Class<?> cacheOptClass();

    /**
     * 是否为删除操作
     *
     * @return true 是    false 否  默认 false
     */
    boolean isRemoveOpt() default false;

}
