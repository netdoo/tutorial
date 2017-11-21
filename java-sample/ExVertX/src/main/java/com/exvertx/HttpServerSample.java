package com.exvertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jhkwon78 on 2017-11-21.
 */
public class HttpServerSample {
    final static Logger logger = LoggerFactory.getLogger(HttpServerSample.class);

    static class HttpServerVerticle extends AbstractVerticle {
        boolean isStart = false;
        boolean isStop = false;
        HttpServer httpServer = null;
        Router router = null;

        @Override
        public void start() throws Exception {
            logger.info("Start HttpServer Verticle");
            this.isStart = true;
            this.httpServer = this.vertx.createHttpServer();
            this.router = Router.router(this.vertx);
            this.router.route().handler(BodyHandler.create());
            this.router.route(HttpMethod.GET, "/echo").handler(new Handler<RoutingContext>() {
                @Override
                public void handle(RoutingContext event) {
                    HttpServerRequest request = event.request();
                    String param = request.getParam("param");
                    HttpServerResponse response = request.response();
                    response.setStatusCode(200);
                    response.headers()
                            .add("Content-Length", String.valueOf(param.length()))
                            .add("Content-Type", "text/html");
                    response.write(param.toUpperCase());
                    response.end();
                }
            });

            this.router.route(HttpMethod.POST, "/echo").handler(new Handler<RoutingContext>() {
                @Override
                public void handle(RoutingContext event) {
                    HttpServerRequest request = event.request();
                    String param = request.getFormAttribute("param");
                    HttpServerResponse response = request.response();
                    response.setStatusCode(200);
                    response.headers()
                            .add("Content-Length", String.valueOf(param.length()))
                            .add("Content-Type", "text/html");
                    response.write(param.toUpperCase());
                    response.end();
                }
            });

            this.httpServer.requestHandler(router::accept).listen(9999);
            logger.info("start http server verticle port : 9999");
        }

        @Override
        public void stop() throws Exception {
            logger.info("Stop HttpServer Verticle");

            this.httpServer.close();
            this.isStop = true;
        }

        public boolean isStart() {
            return this.isStart;
        }
        public boolean isStop() {
            return this.isStop;
        }
    }

    public static void main( String[] args ) throws Exception {
        Vertx vertx = Vertx.vertx();

        HttpServerVerticle httpServerVerticle = new HttpServerVerticle();
        vertx.deployVerticle(httpServerVerticle);

        while (!httpServerVerticle.isStart()) {
            Thread.sleep(500);
            logger.info("wait for start http server verticle ..");
        }

        // > curl localhost:9999/echo?param=hello
        Thread.sleep(60_000);

        String deploymentID = httpServerVerticle.deploymentID();
        logger.info("deploymentID {}", deploymentID);
        vertx.undeploy(deploymentID);

        while(!httpServerVerticle.isStop()) {
            Thread.sleep(500);
            logger.info("wait for stop http server verticle ..");
        }

        vertx.close();
    }
}
