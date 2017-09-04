package com.exprop.domain;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class World {
    final String greeting;

    @Autowired
    public World(@Value("${greeting}") final String greeting) {
        this.greeting = greeting;
    }

    public String getGreeting() {
        return this.greeting;
    }
}
