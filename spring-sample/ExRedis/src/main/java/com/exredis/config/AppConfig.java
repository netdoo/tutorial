package com.exredis.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.cache.annotation.CachingConfigurerSupport;

@Configuration
@ImportResource("classpath:/app-config.xml")
@ComponentScan(basePackages = {"com.exredis"})
@EnableAspectJAutoProxy(proxyTargetClass=false)
public class AppConfig  {

}
