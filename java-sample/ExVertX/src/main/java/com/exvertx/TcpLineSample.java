package com.exvertx;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpLineSample {
    final static Logger logger = LoggerFactory.getLogger(TcpLineSample.class);

    public static void main( String[] args ) {

        Vertx vertx = Vertx.vertx();
        NetServerOptions options = new NetServerOptions();
        options.setReusePort(true).setReuseAddress(true).setPort(1234);
        NetServer server = vertx.createNetServer(options);

        server.connectHandler( netSocket -> {
            logger.info("Incoming connection {}", netSocket.remoteAddress().toString());

            netSocket.handler(RecordParser.newDelimited("\n", buffer -> {
                String incomingData = buffer.getString(0, buffer.length());
                logger.info("incomingData {}", incomingData);
                Buffer outBuffer = Buffer.buffer();
                outBuffer.appendString(incomingData);
                netSocket.write(outBuffer);
            }));

            netSocket.closeHandler(Void -> {
                logger.info("close peer {}:{}", netSocket.remoteAddress().host(), netSocket.remoteAddress().port());
            });
        });

        Util.waitAfterSafeStop(20, server, vertx);
    }
}

