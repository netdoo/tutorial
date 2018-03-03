package com.exprofile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("real")
public class RealConfig {
    @Bean
    public String myName() {
        return new String("real");
    }
}
