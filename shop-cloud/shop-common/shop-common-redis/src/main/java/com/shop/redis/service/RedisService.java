package com.shop.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/10 22:57
 * @Version: 1.0
 * @Description:
 */
@Component
public class RedisService {
    @Autowired
    public RedisTemplate redisTemplate;

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    public boolean deleteObject(String key) {
        return redisTemplate.delete(key);
    }
    public <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }
}
