package com.exreact;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * Unit test for Mono
 */
public class MonoTest {

    final static Logger logger = LoggerFactory.getLogger(MonoTest.class);

    @Test
    public void testMonoEmpty() throws Exception {
        Mono.empty().subscribe(System.out::println);
    }

    @Test
    public void testMonoJust() throws Exception {
        Mono.just("JSA")
                .map(item -> "Mono " + item)
                .subscribe(item -> {
                    logger.info("{}", item);
                });
    }

    @Test
    public void testMonoException() {
        Mono.error(new RuntimeException("Test Mono Exception"))
                .doOnError(e -> {
                    logger.info("catch exception {}", e.getMessage());
                })
                .subscribe(System.out::println);
    }
}
