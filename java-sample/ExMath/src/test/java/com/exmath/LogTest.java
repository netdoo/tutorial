package com.exmath;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogTest {

    final static Logger logger = LoggerFactory.getLogger(LogTest.class);

    double getLogWeight(double qcNow, double qcPrev) {
        return qcNow + (qcNow * ((Math.log10(qcNow) - Math.log10(qcPrev))/2));
    }

    @Test
    public void _0_테스트_준비() throws Exception {
        logger.info("weight {}", getLogWeight(200, 100));
    }
}
