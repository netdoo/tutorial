package com.exre;


import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TripleTest {

    final Logger logger = LoggerFactory.getLogger(TripleTest.class);

    @Test
    public void testTriple() {

        Triple<String, String, String> triple = new ImmutableTriple<>("a", "b", "c");

        Assert.assertTrue(triple.getLeft().contentEquals("a"));
        Assert.assertTrue(triple.getMiddle().contentEquals("b"));
        Assert.assertTrue(triple.getRight().contentEquals("c"));

        logger.info("{} {} {}", triple.getLeft(), triple.getMiddle(), triple.getRight());
    }
}
