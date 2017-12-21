package com.exredis;

import com.exredis.domain.Box;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Lazy
public class SpringRedis {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    ObjectMapper mapper;

    public SpringRedis() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void flushAll() {
        this.redisTemplate.execute((RedisCallback<Object>)redisConnection -> {
            redisConnection.flushAll();
            return null;
        });
    }

    public void setValue(String key, String value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    public void setValue(String key, String value, long timeout, TimeUnit unit) {
        this.redisTemplate.opsForValue().set(key, value);
        this.redisTemplate.expire(key, timeout, unit);
    }

    public String getValue(String key) {
        return (String)this.redisTemplate.opsForValue().get(key);
    }

    public void setObject(String key, Object box) throws Exception {
        String value = this.mapper.writeValueAsString(box);
        this.redisTemplate.opsForValue().set(key, value);
    }

    public Object getObject(String key, TypeReference valueType) throws Exception {
        return this.mapper.readValue((String)this.redisTemplate.opsForValue().get(key), valueType);
    }

    public <T> T getObject(String key, Class<T> valueType) throws Exception {
        return this.mapper.readValue((String)this.redisTemplate.opsForValue().get(key), valueType);
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

    public void publish(String channel, String message) {
        this.redisTemplate.convertAndSend(channel, message);
    }
}
