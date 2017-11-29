package com.exreact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

public class App {

	final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {

    	HttpServer server = HttpServer.create("localhost", 8080);

		server.newHandler(new ReactorHttpHandlerAdapter(RouterFunctions.toHttpHandler(RouterFunctions.route(
				GET("/"), serverRequest -> {
					return Mono
							.justOrEmpty("Hello Spring 5")
							.flatMap(greeting -> ServerResponse.ok().contentType(TEXT_HTML).body(fromObject(greeting)))
							.switchIfEmpty(ServerResponse.notFound().build());
				})))).block();

		logger.info("Press ENTER to exit.");
		System.in.read();
	}
}

