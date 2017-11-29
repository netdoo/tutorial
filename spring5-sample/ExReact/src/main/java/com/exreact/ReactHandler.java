package com.exreact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.scheduler.Schedulers;


import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Created by jhkwon78 on 2017-11-29.
 */
public class ReactHandler {

    final static Logger logger = LoggerFactory.getLogger(ReactHandler.class);

    void sleepSecs(int secs) {
        try {
            Thread.sleep(secs * 1_000);
        } catch (Exception e) {}
    }

    String someLongTaskJob(String param) {
        for (int i = 0; i < 10; i++) {
            sleepSecs(1);
            logger.info("{} process long task ..", i);
        }
        return param.toUpperCase();
    }

    public Mono<ServerResponse> hello(ServerRequest request) {
        return Mono
                .justOrEmpty(someLongTaskJob("hello"))
                .flatMap(greeting -> ServerResponse.ok().contentType(TEXT_HTML).body(fromObject(greeting)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> world(ServerRequest request) {
        Flux<String> all = Flux.fromIterable(Arrays.asList("MBC", "SBS"));
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(all, String.class);
    }


}
