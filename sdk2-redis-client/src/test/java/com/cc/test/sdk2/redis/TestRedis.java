package com.cc.test.sdk2.redis;

import com.cc.sdk2.jsdk.commons.jackson.JacksonUtils;
import com.cc.sdk2.redis.clients.RedisClient;
import com.cc.sdk2.redis.clients.SingleRedisClient;
import org.junit.Test;

import java.util.Map;

/**
 * Description
 *
 * @author sen.hu
 */
public class TestRedis {

    @Test
    public void test1() {
        RedisClient redisClient = new SingleRedisClient("123.57.229.225:56379", "redis@229.225");
        redisClient.set("test", "{\"code\":0,\"data\":null,\"msg\":\"success\",\"time\":\"2021-02-23 13:33:46\",\"traceId\":\"0575cb09c5074269b5f70f92849f2295\"}");
        Map<String, Object> map = redisClient.get("test", (value)-> JacksonUtils.fromJson(value, Map.class));
        System.out.println(JacksonUtils.toJson(map));
    }

}
