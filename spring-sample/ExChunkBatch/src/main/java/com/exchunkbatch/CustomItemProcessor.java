package com.exchunkbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<String, String> {

    Logger logger = LoggerFactory.getLogger(getClass());
    static boolean once = false;

    @Override
    public String process(String alphabet) throws Exception {

        if (!once) {
            // Processor에서 null을 반환하게 되면,
            // Writer에서 size가 0인 List가 생성되고,
            // 배치는 계속 이어서 실행됨.
            once = true;
            logger.info("return fake null");
            return null;
        }

        String processData = alphabet.toUpperCase();
        logger.info("process {} => {}", alphabet, processData);
        return processData;
    }
}

