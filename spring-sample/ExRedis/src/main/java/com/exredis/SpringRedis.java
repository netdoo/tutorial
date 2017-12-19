package com.exredis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@Lazy
public class SpringRedis {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void flushAll() {
        this.redisTemplate.execute((RedisCallback<Object>)redisConnection -> {
            redisConnection.flushAll();
            return null;
        });
    }

    public void setValue(String key, String value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    public String getValue(String key) {
        return (String)this.redisTemplate.opsForValue().get(key);
    }

    public void delKey(String key) {
        this.redisTemplate.delete(key);
    }

    public void hashSetValue(String key, String field, String value) {
        this.redisTemplate.opsForHash().put(key, (String)field, value);
    }

    public String hashGetValue(String key, String field) {
        return (String)this.redisTemplate.opsForHash().get(key, field);
    }

    public Map<Object, Object> hashEntries(String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

    public void hashDelValue(String key, String field) {
        this.redisTemplate.opsForHash().delete(key, field);
    }

    public void zAdd(String key, String field, double score) {
        this.redisTemplate.opsForZSet().add(key, field, score);
    }

    public void zIncrement(String key, String field, double score) {
        this.redisTemplate.opsForZSet().incrementScore(key, field, score);
    }

    public Set<Object> getTop(String key, int top) {
        return this.redisTemplate.opsForZSet().reverseRange(key, 0, top-1);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getTopWithScore(String key, int top) {
        return this.redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, top-1);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getTopWithScore(String key, int start, int limit) {
        return this.redisTemplate.opsForZSet().reverseRangeWithScores(key, start, start+limit-1);
    }
}
