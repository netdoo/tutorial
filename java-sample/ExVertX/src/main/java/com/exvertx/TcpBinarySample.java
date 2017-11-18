package com.exvertx;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpBinarySample {
    final static Logger logger = LoggerFactory.getLogger(TcpBinarySample.class);
    enum FrameTokenType {
        SIZE, PAYLOAD
    };
    final static int HEADER_SIZE = 1;

    public static void main( String[] args ) {

        Vertx vertx = Vertx.vertx();
        NetServerOptions options = new NetServerOptions();
        options.setReusePort(true).setReuseAddress(true).setPort(1234);
        NetServer server = vertx.createNetServer(options);

        server.connectHandler( netSocket -> {
            logger.info("Incoming connection {}", netSocket.remoteAddress().toString());
            final RecordParser parser = RecordParser.newFixed(HEADER_SIZE);

            Handler<Buffer> handler = new Handler<Buffer>() {
                FrameTokenType expectedToken = FrameTokenType.SIZE;
                @Override
                public void handle(Buffer buffer) {

                    switch (expectedToken) {
                    case SIZE:
                        int payloadSize = Integer.valueOf(buffer.getString(0, buffer.length())).intValue();
                        parser.fixedSizeMode(payloadSize);
                        logger.info("payload size {}", payloadSize);
                        expectedToken = FrameTokenType.PAYLOAD;
                        break;
                    case PAYLOAD:
                        String incomingData = buffer.getString(0, buffer.length());
                        logger.info("incomingData {}", incomingData);
                        Buffer outBuffer = Buffer.buffer();
                        outBuffer.appendString(incomingData);
                        netSocket.write(outBuffer);
                        parser.fixedSizeMode(HEADER_SIZE);
                        expectedToken = FrameTokenType.SIZE;
                        break;
                    }
                }
            };

            parser.setOutput(handler);
            netSocket.handler(parser);

            netSocket.closeHandler(Void -> {
                logger.info("close peer {}:{}", netSocket.remoteAddress().host(), netSocket.remoteAddress().port());
            });
        });

        Util.waitAfterSafeStop(2000, server, vertx);
    }
}

