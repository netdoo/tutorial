package com.exmath;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    String getLogWeight(Long qcNow, Long qcPrev) {
        if (qcPrev == 0) {
            qcPrev = 1L;
        }

        double result = (qcNow * ((Math.log10(qcNow) - Math.log10(qcPrev))/2));
        return String.format("%.2f" , result);
    }

    @Test
    public void _0_테스트_준비() throws Exception {
        logger.info("weight {}", getLogWeight(200L, 100L));
        logger.info("weight {}", getLogWeight(200L, 0L));
        logger.info("weight {}", getLogWeight(30L, 0L));
    }
}
