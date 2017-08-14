package com.example.simplecui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class DemoApplication {

	@Bean
	String info() {
		return "my info string";
	}

	@Autowired
	String info;

	public static void main(String[] args) {
		new SpringApplicationBuilder(DemoApplication.class)
				.bannerMode(Banner.Mode.OFF)
				.web(false)
				.run(args);
	}

	@Bean
	CommandLineRunner myMethod() {
		return new CommandLineRunner() {
			@Override
			public void run(String... strings) throws Exception {
				Arrays.stream(strings).forEach(arg -> {
					System.out.println(arg);
				});

                System.out.println("info : " + info);
            }
		};
	}
}
