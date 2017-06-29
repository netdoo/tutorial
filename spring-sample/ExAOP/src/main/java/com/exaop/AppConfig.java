package com.exaop;

import org.springframework.context.annotation.*;

@Configuration
@ImportResource("classpath:/app-config.xml")
@ComponentScan("com.exaop")
@EnableAspectJAutoProxy(proxyTargetClass=false)
public class AppConfig {

    @Bean(name = "someObject")
    public SomeObject someObject() {
        return new SomeObject();
    }
}
