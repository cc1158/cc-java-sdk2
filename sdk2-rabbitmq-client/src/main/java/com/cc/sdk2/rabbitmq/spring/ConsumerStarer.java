package com.cc.sdk2.rabbitmq.spring;

import com.cc.sdk2.rabbitmq.MessageProcessor;
import com.cc.sdk2.rabbitmq.consumer.RabbitMQConsumer;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/1/16 18:24
 **/
public class ConsumerStarer {

    @Autowired(required = false)
    private List<MessageProcessor> consumerProcessors;

    @EventListener
    public <E> void onApplicationReady(ApplicationReadyEvent readyEvent) {
        if (CollectionUtils.isEmpty(consumerProcessors)) {
            return;
        }

        ApplicationContext context = readyEvent.getApplicationContext();
        for (MessageProcessor processor : consumerProcessors) {
            String id = extractConsumerId(processor);
            RabbitMQConsumer rabbitMQConsumer = context.getBean(id, RabbitMQConsumer.class);
            try {
                rabbitMQConsumer.startConsuming(processor);
            } catch (Exception e) {
            }
        }
    }

    private String extractConsumerId(MessageProcessor processor) {
        Class<? extends MessageProcessor> clazz = (Class<? extends MessageProcessor>) AopUtils.getTargetClass(processor);
        ConsumerId consumerId = clazz.getDeclaredAnnotation(ConsumerId.class);
        String id = consumerId.id();
        if (id.isEmpty()) {
            throw new IllegalArgumentException(processor.getClass().getName() + "@ConsumerId  id is Null");
        }
        return id;

    }

//    private Type extractEventType(MessageProcessor processor) {
//        Class<? extends MessageProcessor> clazz = (Class<? extends MessageProcessor>) AopUtils.getTargetClass(processor);
//        return TypeUtils.getTypeParameter(clazz, EventProcessor.class, 0);
//    }

}
