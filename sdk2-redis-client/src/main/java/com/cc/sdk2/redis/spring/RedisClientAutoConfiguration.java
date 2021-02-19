package com.cc.sdk2.redis.spring;

import com.cc.sdk2.redis.clients.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties({RedisClientConfigurationProperties.class})
public class RedisClientAutoConfiguration {

    @Bean(name = "ccRedisClient")
    @ConditionalOnProperty(prefix = "cc-redis", name = {"enabled"}, matchIfMissing = true)
    public RedisClient createRedisClient(RedisClientConfigurationProperties properties) {
        RedisClient redisClient;
        try {
            if (properties.isCluster()) {
                redisClient = new ClusterRedisClient(properties.getHosts(), properties.getAuth(), properties.getConnTimeout(), properties.getSoTimeout(), properties.getMaxTries());
            } else {
                redisClient = new SingleRedisClient(properties.getHosts(), properties.getAuth(), properties.getConnTimeout(), properties.getSoTimeout());
            }
            return redisClient;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(String.format("CCRedisClient initialized failed: %s", e.getMessage()));
        }
    }


}
