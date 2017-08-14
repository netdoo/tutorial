package com.extiles;

import com.extiles.config.ServerCustomization;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExtilesApplication {
	/*
	@Bean
	public ServerProperties getServerProperties() {
		return new ServerCustomization();
	}*/
	public static void main(String[] args) {
		SpringApplication.run(ExtilesApplication.class, args);
	}
}
