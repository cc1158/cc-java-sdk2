package com.cc.sdk2.rabbitmq;

import java.util.Map;

/**
 * All rights reserved, copyright@cc.hu
 * 消费者实现
 * @author cc
 * @version 1.0
 * @date 2021/1/16 22:23
 **/
public interface MessageProcessor {
    void process(String routingKey, long deliveryTag, Map<String, Object> headers, byte[] body);
}
