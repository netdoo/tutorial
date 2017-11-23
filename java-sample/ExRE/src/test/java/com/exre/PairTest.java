package com.exre;


import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PairTest {

    final Logger logger = LoggerFactory.getLogger(PairTest.class);

    @Test
    public void testPair() {

        Pair<String, String> pair = new ImmutablePair<>("a", "b");

        Assert.assertTrue(pair.getKey().contentEquals("a"));
        Assert.assertTrue(pair.getValue().contentEquals("b"));

        logger.info("{} {}", pair.getKey(), pair.getValue());
    }
}
