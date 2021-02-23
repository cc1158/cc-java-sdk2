package com.cc.sdk2.redis.clients;

import com.cc.sdk2.redis.serialize.*;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.*;

/**
 * 单点redisclient
 *
 * @author cc.sen
 */
public class SingleRedisClient extends RedisClient {

    private static final int DEFAULT_DATABASE_INDEX = 0;

    private ThreadLocal<Integer> REDIS_DATABASE_INDEX = ThreadLocal.withInitial(() -> DEFAULT_DATABASE_INDEX);
    private JedisPool jedisPool;


    public SingleRedisClient(String hosts) {
        this(hosts, null);
    }

    public SingleRedisClient(String hosts, String auth) {
        this(hosts, auth, DEFAULT_CONN_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }


    public SingleRedisClient(String hosts, String auth, int conTimeout, int soTimeout) {
        this(hosts, auth, conTimeout, soTimeout, null);
    }

    public SingleRedisClient(String hosts, String auth, int conTimeout, int soTimeout, JedisPoolConfig jedisPoolConfig) {
        super(hosts, auth, jedisPoolConfig);
        this.connTimeout = connTimeout > 0 ? conTimeout : DEFAULT_CONN_TIMEOUT;
        this.socketTimeout = soTimeout > 0 ? soTimeout : DEFAULT_SOCKET_TIMEOUT;
        this.initConnection();
    }

    @Override
    protected void initConnection() {
        try {
            initLock.lock();
            if (!this.redisConnStatus) {
                if (this.redisHosts.size() > 0) {
                    for (HostAndPort item : this.redisHosts) {
                        this.jedisPool = new JedisPool(this.jedisPoolConfig, item.getHost(), item.getPort(), this.connTimeout, auth);
                        this.redisConnStatus = true;
                        break;
                    }
                } else {
                    throw new RuntimeException("Redis Address And Port Not Configured");
                }
            }
        } finally {
            initLock.unlock();
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    @Override
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    @Override
    public List<String> time() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            int index = REDIS_DATABASE_INDEX.get() == null ? DEFAULT_DATABASE_INDEX : REDIS_DATABASE_INDEX.get();
            jedis.select(index);
            return jedis.time();
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public void setIndex(int index) {
        if (index < 0 || index > 15) {
            throw new IllegalArgumentException("Redis Index Can't be Negative");
        } else {
            REDIS_DATABASE_INDEX.set(index);
        }
    }

    @Override
    public void setDefaultIndex() {
        REDIS_DATABASE_INDEX.set(DEFAULT_DATABASE_INDEX);
    }

    @Override
    public boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.exists(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public boolean exists(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.exists(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long del(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.del(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long del(String... keys) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.del(keys);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long del(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.del(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long del(byte[]... keys) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.del(keys);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.get(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public <R> R get(String key, StringDeserializer<R> deserializer) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return deserializer.apply(jedis.get(key));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public byte[] get(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.get(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public <R> R get(byte[] key, BytesDeserializer<R> deserializer) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return deserializer.apply(jedis.get(key));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.set(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public String set(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.set(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public long setnx(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.setnx(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public long setnx(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.setnx(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public String setex(String key, int secs, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.setex(key, secs, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public String setex(byte[] key, int secs, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.setex(key, secs, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long expire(String key, int secs) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.expire(key, secs);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long expire(byte[] key, int secs) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.expire(key, secs);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.expireAt(key, unixTime);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long expireAt(byte[] key, long unixTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.expireAt(key, unixTime);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long llen(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.llen(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long llen(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.llen(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long lpush(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.lpush(key, values);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long lpush(byte[] key, byte[]... values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.lpush(key, values);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public String rpop(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.rpop(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public <R> R rpop(String key, StringDeserializer<R> deserializer) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return deserializer.apply(jedis.get(key));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public byte[] rpop(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.rpop(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public <R> R rpop(byte[] key, BytesDeserializer<R> deserializer) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return deserializer.apply(jedis.rpop(key));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long rpush(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.rpush(key, values);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long rpush(byte[] key, byte[]... values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.rpush(key, values);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public String lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.lpop(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public <R> R lpop(String key, StringDeserializer<R> deserializer) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return deserializer.apply(jedis.get(key));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public byte[] lpop(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.lpop(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public <R> R lpop(byte[] key, BytesDeserializer<R> deserializer) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return deserializer.apply(jedis.get(key));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.lrange(key, start, end);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public <R> List<R> lrange(String key, long start, long end, StringDeserializer<R> deserializer) {
        Jedis jedis = null;
        List<R> list = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            List<String> valueList = jedis.lrange(key, start, end);
            if (valueList != null) {
                list = new ArrayList<>();
                for (String item : valueList) {
                    list.add(deserializer.apply(item));
                }
            }
            return list;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public List<byte[]> lrange(byte[] key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.lrange(key, start, end);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public <R> List<R> lrang(byte[] key, long start, long end, BytesDeserializer<R> deserializer) {
        Jedis jedis = null;
        List<R> list = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            List<byte[]> valueList = jedis.lrange(key, start, end);
            if (valueList != null) {
                list = new ArrayList<>();
                for (byte[] item : valueList) {
                    list.add(deserializer.apply(item));
                }
            }
            return list;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.incr(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long incr(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.incr(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long incrBy(String key, long incrs) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.incrBy(key, incrs);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long decr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.decr(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long decr(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.decr(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long decrBy(String key, long decrs) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.decrBy(key, decrs);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hget(key, field);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public <R> R hget(String key, String field, StringDeserializer<R> deserializer) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return deserializer.apply(jedis.hget(key, field));
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hget(key, field);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hgetAll(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hgetAll(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long hset(String key, Map<String, String> values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hset(key, values);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hset(key, field, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long hset(byte[] key, Map<byte[], byte[]> values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hset(key, values);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hsetnx(key, field, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hsetnx(key, field, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hsetnx(key, field, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hmset(key, hash);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hmset(key, hash);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hmget(key, fields);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.hmget(key, fields);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public long sadd(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.sadd(key, members);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public long sadd(byte[] key, byte[]... members) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.sadd(key, members);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Set<String> smembers(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.smembers(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Set<byte[]> smembers(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            return jedis.smembers(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public boolean getDistributeLock(String key, String requestId, int expSec) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            String result = jedis.set(key, requestId, SetParams.setParams().nx().ex(expSec));
            if (result.equals(GET_LOCK_SUCCESS)) {
                return true;
            }
            return false;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public boolean releaseDistributeLock(String key, String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(REDIS_DATABASE_INDEX.get());
            Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(requestId));
            if (result.equals(RELEASE_LOCK_SUCCESS)) {
                return true;
            }
            return false;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }
}
