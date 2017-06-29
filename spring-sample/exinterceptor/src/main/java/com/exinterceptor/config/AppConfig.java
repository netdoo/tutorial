package com.exinterceptor.config;

import com.exinterceptor.interceptor.LoginSessionCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;
import com.exinterceptor.interceptor.UpperInterceptor;

@Configuration
public class AppConfig {//extends WebMvcConfigurerAdapter {
    @Bean
    public MappedInterceptor upperInterceptor() {
        return new MappedInterceptor(new String[]{"/upper"}, new UpperInterceptor());
    }

    @Bean
    public MappedInterceptor loginInterceptor() {
        return new MappedInterceptor(new String[]{"/home", "/admin"}, new LoginSessionCheckInterceptor());
    }
}
