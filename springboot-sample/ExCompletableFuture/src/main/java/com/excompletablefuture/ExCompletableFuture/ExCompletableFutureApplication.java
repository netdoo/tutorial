package com.excompletablefuture.ExCompletableFuture;

import com.excompletablefuture.ExCompletableFuture.service.MyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@EnableAsync
public class ExCompletableFutureApplication {

	final static Logger logger = LoggerFactory.getLogger(ExCompletableFutureApplication.class);

	public static void main(String[] args) {
		new SpringApplicationBuilder(ExCompletableFutureApplication.class)
				.bannerMode(Banner.Mode.OFF)
				.web(false)
				.run(args);
	}

	@Bean
	SimpleAsyncTaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	@Bean
	CommandLineRunner myMethod() {
		return new CommandLineRunner() {

			@Autowired
			MyService myService;

			@Override
			public void run(String... strings) throws Exception {

				/// @EnableAsync 어노테이션이 있으면, 아래 3개의 작업이 동시에 실행이 되고,
				/// @EnableAsync 어노테이션이 없으면, 아래 3개의 작업이 순서대로 실행이 됨.
				CompletableFuture<String> foo = this.myService.getFoo();
				CompletableFuture<String> bar = this.myService.getBar();
				CompletableFuture<String> zoo = this.myService.getZoo();

				CompletableFuture.allOf(foo, bar, zoo).join();

				logger.info("#1 foo {} bar {} zoo {}", foo.get(), bar.get(), zoo.get());

				/// 각각의 쓰레드에서 동기함수가 순서대로 실행됨.
				logger.info("#2 foo {} bar {} zoo {}", myService.getFoo().get(), myService.getBar().get(), myService.getZoo().get());

				/// 메인쓰레드에서, 동기함수가 순서대로 실행됨.
				logger.info("#3 foo {} bar {} zoo {}", myService.foo(), myService.bar(), myService.zoo());
			}
		};
	}
}
