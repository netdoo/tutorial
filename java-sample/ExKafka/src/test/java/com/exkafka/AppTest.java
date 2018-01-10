package com.exkafka;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    @Test
    public void _0_테스트준비() throws Exception {

    }

    @Test
    public void _1_테스트_Produce_Consume() {
        Class[] cls = { ConsumerTest.class, ProducerTest.class };
        JUnitCore.runClasses(new ParallelComputer(true, true), cls);
    }
}
