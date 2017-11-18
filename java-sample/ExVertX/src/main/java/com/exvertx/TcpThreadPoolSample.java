package com.exvertx;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TcpThreadPoolSample {
    final static Logger logger = LoggerFactory.getLogger(TcpThreadPoolSample.class);

    static class SocketTask implements Runnable {
        NetSocket netSocket;
        String incomingData;

        public SocketTask(NetSocket netSocket, String incomingData) {
            this.netSocket = netSocket;
            this.incomingData = incomingData;
        }

        @Override
        public void run() {
            logger.info("process start {}", netSocket.remoteAddress().toString());

            try {
                for (int i = 0; i < 5; i++) {
                    logger.info("process some long task...");
                    Thread.sleep(1_000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Buffer outBuffer = Buffer.buffer();
            outBuffer.appendString(this.incomingData);
            netSocket.write(outBuffer);
            logger.info("process finish {}", netSocket.remoteAddress().toString());
        }
    }

    public static void main( String[] args ) {

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

        Vertx vertx = Vertx.vertx();
        NetServerOptions options = new NetServerOptions();
        options.setReusePort(true).setReuseAddress(true).setPort(1234);
        NetServer server = vertx.createNetServer(options);

        server.connectHandler( netSocket -> {
            logger.info("Incoming connection {}", netSocket.remoteAddress().toString());

            netSocket.handler(RecordParser.newDelimited("\n", buffer -> {
                String incomingData = buffer.getString(0, buffer.length());
                logger.info("incomingData {}", incomingData);
                executorService.schedule(new SocketTask(netSocket, incomingData), 2, TimeUnit.SECONDS);
            }));

            netSocket.closeHandler(Void -> {
                logger.info("close peer {}:{}", netSocket.remoteAddress().host(), netSocket.remoteAddress().port());
            });
        });

        Util.waitAfterSafeStop(50, server, vertx);
        executorService.shutdown();
        try {
            executorService.awaitTermination(6, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

