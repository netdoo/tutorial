package com.exmemcache.config;

import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource("classpath:/memcached.properties")
public class MemcachedConfig {

    @Value("${memcached.hostname}")
    String memcachedHostName;

    @Value("${memcached.port}")
    int memcachedPort;

    @Bean
    public MemcachedClient memcachedClient() throws Exception {

        List<InetSocketAddress> addressList = new ArrayList<>();
        addressList.add(new InetSocketAddress(memcachedHostName, memcachedPort));
        addressList.add(new InetSocketAddress("localhost", 11222));
        MemcachedClient memcachedClient = new MemcachedClient(addressList);

        return memcachedClient;
    }
}
