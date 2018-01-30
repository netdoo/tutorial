package com.exredis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("classpath:/redis.properties")
public class RedisConfig {

    @Value("${redis.hostname}")
    String redisHostName;

    @Value("${redis.port}")
    int redisPort;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool pool = new JedisPool(jedisPoolConfig, redisHostName, redisPort, 1000);
        return pool;
    }
}
