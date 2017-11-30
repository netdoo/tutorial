package com.exreact;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Unit test for Flux
 */
public class FluxTest {

    final static Logger logger = LoggerFactory.getLogger(FluxTest.class);

    @Test
    public void testFluxEmpty() throws Exception {
        Flux.empty().subscribe(System.out::println);
    }

    @Test
    public void testFluxJust() throws Exception {
        Flux.just("Red", "Green", "Blue")
                .map(item -> item.toUpperCase())
                .subscribe(item -> {
                    logger.info("{}", item);
                });
    }

    @Test
    public void testFluxFromList() {
        Flux.fromIterable(Arrays.asList("Red", "Green", "Blue"))
                .map(item -> item.toUpperCase())
                .subscribe(item -> {
                    logger.info("{}", item);
                });
    }

    @Test
    public void testFluxInterval() throws Exception {
        Flux.interval(Duration.ofMillis(100))
                .map(item -> "tick : " + item)
                .take(5)
                .subscribe(item -> {
                    logger.info("{}", item);
                });

        Thread.sleep(1500);
    }

    @Test
    public void testFluxException() {
        Flux.error(new RuntimeException("Test Flux Exception"))
                .doOnError(e -> {
                    logger.info("catch exception {}", e.getMessage());
                })
                .subscribe(System.out::println);
    }

    @Test
    public void testFluxList() {
        Flux<String> flux = Flux.just("Red", "Green", "Blue");
        List<String> list = flux.collectList().block();
        list.forEach(item -> {
            logger.info("{}", item);
        });
    }

    @Test
    public void testFluxSortedList() {
        Flux<String> flux = Flux.just("Red", "Green", "Blue");
        List<String> list = flux.collectSortedList().block();
        list.forEach(item -> {
            logger.info("{}", item);
        });
    }

    @Test
    public void testFluxMap() {
        Flux<String> flux = Flux.just("0:Red", "1:Green", "2:Blue");
        Map<String, String> map = flux.collectMap(item -> {
            return item.split(":")[0];
        }, item -> {
            return item.split(":")[1];
        }).block();
        map.forEach((key, value) -> {
            logger.info("{}/{}", key, value);
        });
    }

    @Test
    public void testFluxConcat() {
        Flux<String> colorEng = Flux.just("Red", "Green", "Blue");
        Flux<String> colorKor = Flux.just("빨강", "초록", "파랑");

        Flux.concat(colorEng, colorKor)
            .subscribe(result -> {
                logger.info("{}", result);
            });
    }

    @Test
    public void testFluxMerge() {
        Flux<String> colorEng = Flux.just("Red", "Green", "Blue");
        Flux<String> colorKor = Flux.just("빨강", "초록", "파랑");

        Flux.merge(colorEng, colorKor)
                .subscribe(result -> {
                    logger.info("{}", result);
                });
    }

    @Test
    public void testFluxZip() {
        Flux<String> colorEng = Flux.just("Red", "Green", "Blue");
        Flux<String> colorKor = Flux.just("빨강", "초록", "파랑");

        colorEng.zipWith(colorKor, (eng, kor) -> {
            return eng + ":" + kor;
        }).subscribe(result -> {
            logger.info("{}", result);
        });
    }
}
