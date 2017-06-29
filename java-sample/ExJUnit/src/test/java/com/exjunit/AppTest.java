package com.exjunit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {

    public AppTest( String testName ) {
        super(testName);
    }

    public static Test suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTestSuite(AppTest.class);
        testSuite.addTestSuite(FooTest.class);
        testSuite.addTestSuite(BarTest.class);
        return testSuite;
    }

    public void testApp() {
        assertTrue(true);
    }
}
