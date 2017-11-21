package com.exvertx;

import io.vertx.core.buffer.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BufferSample {
    final static Logger logger = LoggerFactory.getLogger(BufferSample.class);

    public static void main( String[] args ) throws Exception {

        // vertx 에서 사용하는 버퍼에는 다음과 같이 쓰기 연산이 가능하고
        Buffer writeBuffer = Buffer.buffer();

        writeBuffer.appendInt(1);
        writeBuffer.appendLong(2);

        // 다음과 같이 읽기 연산이 가능하다.
        Buffer readBuffer = Buffer.buffer(writeBuffer.getBytes());

        int pos = 0;
        int intVal = readBuffer.getInt(pos);
        pos += Integer.BYTES;

        long longVal = readBuffer.getLong(pos);
        pos += Long.BYTES;

        logger.info("int {} long {}", intVal, longVal);
    }
}

