package com.cc.sdk2.rabbitmq.spring;

import com.cc.sdk2.rabbitmq.spring.registry.RabbitMQRegister;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * All rights reserved, copyright@cc.hu
 * rabbitmq 生产者 消费者 自动化配置是
 * @author cc
 * @version 1.0
 * @date 2019/10/31 20:48
 **/

@Configuration
@EnableConfigurationProperties({RabbitMQProperties.class})
@Import(RabbitMQRegister.class)
@ConditionalOnProperty(prefix = "cc-rabbitmq")
public class RabbitMQAutoConfiguration {

    @Bean
    public ConsumerStarer consumerStarer() {
        return new ConsumerStarer();
    }

}
