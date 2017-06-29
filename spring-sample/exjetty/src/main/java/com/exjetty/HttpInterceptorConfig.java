package com.exjetty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class HttpInterceptorConfig extends WebMvcConfigurerAdapter {

    @Bean
    public MappedInterceptor httpInterceptor() {
        return  new MappedInterceptor(new String[]{"/jetty2"}, new HttpInterceptor2());
    }

/*
    @Bean
    public HttpInterceptor httpInterceptor() {
        return  new HttpInterceptor();
    }

    @Autowired
    HttpInterceptor httpInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpInterceptor)
                .addPathPatterns("/**");
                //.addPathPatterns("/**")
                //.excludePathPatterns("/public/**");
    }
    */
}
