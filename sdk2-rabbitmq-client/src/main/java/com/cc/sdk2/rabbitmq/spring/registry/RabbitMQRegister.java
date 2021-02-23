package com.cc.sdk2.rabbitmq.spring.registry;

import com.cc.sdk2.jsdk.commons.utils.StringUtil;
import com.cc.sdk2.rabbitmq.RabbitMQClient;
import com.cc.sdk2.rabbitmq.consumer.RabbitMQConsumer;
import com.cc.sdk2.rabbitmq.producer.RabbitMQProducer;
import com.cc.sdk2.rabbitmq.spring.RabbitMQProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/1/16 12:25
 **/
public class RabbitMQRegister implements EnvironmentAware, ImportBeanDefinitionRegistrar {

    private RabbitMQProperties rabbitProperties = new RabbitMQProperties();
    private Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
        bindProperties();
    }

    void bindProperties() {
        Binder.get(this.env).bind("cc-rabbitmq", Bindable.ofInstance(rabbitProperties));
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerRabbitMQClient(registry);
        registerProducers(registry);
        registerConsumers(registry);
    }

    void registerRabbitMQClient(BeanDefinitionRegistry registry) {
        BeanDefinition definition = BeanDefinitionBuilder
                .genericBeanDefinition(RabbitMQClient.class, this::createRabbitMQClient)
                .getBeanDefinition();
        registry.registerBeanDefinition(rabbitProperties.getClientName(), definition);
    }

    void registerProducers(BeanDefinitionRegistry registry) {
        if (CollectionUtils.isEmpty(rabbitProperties.getProducers())) {
            return;
        }
        //注册生产者
        rabbitProperties.getProducers().forEach(item -> {
            if (StringUtil.isNullOrEmpty(item.getId())
                    || StringUtil.isNullOrEmpty(item.getExchange())
                    || StringUtil.isNullOrEmpty(item.getRoutingKey())) {
                throw new RuntimeException("请检查RabbitMQ消费者配置: id, exchange, routingKey不能为空");
            }
            BeanDefinition definition = BeanDefinitionBuilder
                    .rootBeanDefinition(RabbitMQProducer.class)
                    .setLazyInit(false)
                    .addConstructorArgReference(rabbitProperties.getClientName())
                    .addConstructorArgValue(item)
                    .getBeanDefinition();
            registry.registerBeanDefinition(item.getId(), definition);
        });

    }

    void registerConsumers(BeanDefinitionRegistry registry) {
        if (CollectionUtils.isEmpty(rabbitProperties.getConsumers())) {
            return;
        }
        //注册消费者
        rabbitProperties.getConsumers().forEach(item -> {
            if (StringUtil.isNullOrEmpty(item.getId())
                    || StringUtil.isNullOrEmpty(item.getQueue())
                    || StringUtil.isNullOrEmpty(item.getRoutingKey())) {
                throw new RuntimeException("请检查RabbitMQ消费者配置: id, queue, routingKey不能为空");
            }
            BeanDefinition definition = BeanDefinitionBuilder
                    .rootBeanDefinition(RabbitMQConsumer.class)
                    .setLazyInit(false)
                    .addConstructorArgReference(rabbitProperties.getClientName())
                    .addConstructorArgValue(item)
                    .setDestroyMethodName(RabbitMQConsumer.destroyMethod)
                    .getBeanDefinition();
            registry.registerBeanDefinition(item.getId(), definition);
        });

    }

    RabbitMQClient createRabbitMQClient() {
        return new RabbitMQClient(this.rabbitProperties.getUser(), this.rabbitProperties.getPassword(), this.rabbitProperties.getHosts(), this.rabbitProperties.getVHost());
    }
}
