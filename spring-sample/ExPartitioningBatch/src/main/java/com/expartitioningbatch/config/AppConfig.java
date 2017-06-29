package com.expartitioningbatch.config;

import com.expartitioningbatch.repository.AlphabetDB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:applicationContext.xml", "classpath:batch/job.xml"})
public class AppConfig {

    @Bean
    AlphabetDB alphabetDB() {
        return new AlphabetDB();
    }
}
