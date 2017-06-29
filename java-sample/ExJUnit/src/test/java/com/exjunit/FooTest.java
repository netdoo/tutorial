package com.exjunit;

import junit.framework.TestCase;

public class FooTest extends TestCase {
    public FooTest(String testName) {
        super(testName);
    }

    public void testFoo() {
        assertTrue(true);
    }
}
