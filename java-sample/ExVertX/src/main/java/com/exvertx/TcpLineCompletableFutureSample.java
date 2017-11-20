package com.exvertx;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

public class TcpLineCompletableFutureSample {
    final static Logger logger = LoggerFactory.getLogger(TcpLineCompletableFutureSample.class);

    static String getTimeText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

    static void doResponse(NetSocket netSocket, String response) {
        Buffer outBuffer = Buffer.buffer();
        outBuffer.appendString(response);
        netSocket.write(outBuffer);
    }

    static void asyncResponseTime(NetSocket netSocket) {
        CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5_000);
                return getTimeText();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }).thenAccept(result -> {
            doResponse(netSocket, result);
        });
    }

    static void responseTime(NetSocket netSocket) {

        try {
            // 의도적으로 5초간 딜레이를 주고 응답을 하게 되면, 다음과 같은 경고가 발생한다.
            // 11월 20, 2017 12:32:28 오후 io.vertx.core.impl.BlockedThreadChecker
            // 경고: Thread Thread[vert.x-eventloop-thread-1,5,main] has been blocked for 2959 ms, time limit is 2000
            // 11월 20, 2017 12:32:29 오후 io.vertx.core.impl.BlockedThreadChecker
            // 경고: Thread Thread[vert.x-eventloop-thread-1,5,main] has been blocked for 3959 ms, time limit is 2000
            // 11월 20, 2017 12:32:30 오후 io.vertx.core.impl.BlockedThreadChecker
            // 경고: Thread Thread[vert.x-eventloop-thread-1,5,main] has been blocked for 4959 ms, time limit is 2000
            Thread.sleep(5_000);
            doResponse(netSocket, getTimeText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) {

        Vertx vertx = Vertx.vertx();
        NetServerOptions options = new NetServerOptions();
        options.setReusePort(true).setReuseAddress(true).setPort(1234);
        NetServer server = vertx.createNetServer(options);

        server.connectHandler( netSocket -> {
            logger.info("Incoming connection {}", netSocket.remoteAddress().toString());

            netSocket.handler(RecordParser.newDelimited("\n", buffer -> {
                String cmd = buffer.getString(0, buffer.length());
                cmd = cmd.replaceAll("\r", "");
                logger.info("cmd {}", cmd);
                if (StringUtils.equalsIgnoreCase("async", cmd)) {
                    // 비동기 방식으로 요청한 경우
                    asyncResponseTime(netSocket);
                } else {
                    // 동기 방식으로 요청한 경우
                    responseTime(netSocket);
                }
            }));

            netSocket.closeHandler(Void -> {
                logger.info("close peer {}:{}", netSocket.remoteAddress().host(), netSocket.remoteAddress().port());
            });
        });

        Util.waitAfterSafeStop(50, server, vertx);
    }
}

