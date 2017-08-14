package com.exjpa;

import com.exjpa.repository.MemoRepository;
import com.exjpa.repository.MyRepository;
import com.exjpa.domain.Memo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

@SpringBootApplication
public class ExjpaApplication {

	@Bean
	MyRepository myRepository() {
		MyRepository myRepository = new MyRepository();
		myRepository.add("my");
		myRepository.add("first");
		myRepository.add("spring");
		myRepository.add("boot");
		return myRepository;
	}

	@Bean
	InitializingBean makeDummyData(MemoRepository memoRepository) {
		InitializingBean initializingBean = new InitializingBean() {
			@Override
			public void afterPropertiesSet() throws Exception {
				/// 스프링 엔진이 인스턴스 생성후, 초기화할 때 항상 호출됨.
				/// 이곳에 초기화가 필요한 코드를 추가함.
				memoRepository.save(new Memo("안녕"));
				memoRepository.save(new Memo("스프링"));
				memoRepository.save(new Memo("부트"));
			}
		};

		return initializingBean;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ExjpaApplication.class);
		app.setBanner(new Banner() {
			@Override
			public void printBanner(Environment environment, Class<?> aClass, PrintStream printStream) {
				printStream.print("\n\n안녕 스프링 부트 !!\n\n");
			}
		});
		app.run(args);
	}
}
