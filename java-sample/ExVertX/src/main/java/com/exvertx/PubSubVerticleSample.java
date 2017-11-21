package com.exvertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class PubSubVerticleSample {
    final static Logger logger = LoggerFactory.getLogger(PubSubVerticleSample.class);

    static class PubVerticle extends AbstractVerticle {
        boolean isStart = false;
        boolean isStop = false;

        @Override
        public void start() throws Exception {
            logger.info("Start PubVerticle");

            this.isStart = true;
            // publish는 n개의 consumer에게 모두 전달되지만,
            this.vertx.eventBus().publish("packet", "test publish");
            // send는 n개의 consumer중 오직 1개의 consumer에게만 전달이 된다.
            this.vertx.eventBus().send("packet", "test send");
        }

        @Override
        public void stop() throws Exception {
            logger.info("Stop PubVerticle");
            this.isStop = true;
        }

        public boolean isStart() {
            return this.isStart;
        }
        public boolean isStop() {
            return this.isStop;
        }
    }

    static class SubVerticle extends AbstractVerticle {
        boolean isStart = false;
        boolean isStop = false;

        @Override
        public void start() throws Exception {
            logger.info("Start SubVerticle");
            this.isStart = true;
            // 첫번째 인자는 수신받을 주소이고, 두번째 인자는 수신받은 메세지이다.
            this.vertx.eventBus().consumer("packet", new Handler<Message<String>>() {
                @Override
                public void handle(Message<String> message) {
                    logger.info("consume id {} address {} body {}", SubVerticle.super.deploymentID(), message.address(), message.body());
                }
            });
        }

        @Override
        public void stop() throws Exception {
            logger.info("Stop SubVerticle");
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

        // 서로 다른 verticle 사이에 통신은 event bus를 사용하여 가능하다.
        SubVerticle subVerticle1 = new SubVerticle();
        SubVerticle subVerticle2 = new SubVerticle();
        vertx.deployVerticle(subVerticle1);
        vertx.deployVerticle(subVerticle2);

        while (!subVerticle1.isStart() && !subVerticle2.isStart()) {
            logger.info("wait for start sub verticle...");
            Thread.sleep(500);
        }

        PubVerticle pubVerticle = new PubVerticle();

        vertx.deployVerticle(pubVerticle);

        while (!pubVerticle.isStart()) {
            logger.info("wait for start pub verticle...");
            Thread.sleep(500);
        }

        Set<String> deploymentIds = vertx.deploymentIDs();

        deploymentIds.forEach(deploymentId -> {
            vertx.undeploy(deploymentId);
        });

        while (!subVerticle1.isStop() || !subVerticle2.isStop()) {
            logger.info("wait for stop sub verticle...");
            Thread.sleep(500);
        }

        vertx.close();
    }
}

