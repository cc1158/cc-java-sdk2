package com.cc.sdk2.rabbitmq.spring;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/1/17 14:11
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface ConsumerId {
    /**
     * 消费者实例名称
     * @return  消费者配置id
     */
    String id();

}
