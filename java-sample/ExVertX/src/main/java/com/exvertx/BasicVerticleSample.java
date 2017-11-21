package com.exvertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

public class BasicVerticleSample {
    final static Logger logger = LoggerFactory.getLogger(BasicVerticleSample.class);

    static class FirstVerticle extends AbstractVerticle {
        boolean isStart = false;
        boolean isStop = false;

        @Override
        public void start() throws Exception {
            logger.info("Start FirstVerticle");
            this.isStart = true;
        }

        @Override
        public void stop() throws Exception {
            logger.info("Stop FirstVerticle");
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

        FirstVerticle firstVerticle = new FirstVerticle();
        vertx.deployVerticle(firstVerticle);

        while (!firstVerticle.isStart()) {
            Thread.sleep(500);
            logger.info("wait for start verticle ..");
        }

        String deploymentID = firstVerticle.deploymentID();
        logger.info("deploymentID {}", deploymentID);
        vertx.undeploy(deploymentID);

        while(!firstVerticle.isStop()) {
            Thread.sleep(500);
            logger.info("wait for stop verticle ..");
        }

        vertx.close();
    }
}

