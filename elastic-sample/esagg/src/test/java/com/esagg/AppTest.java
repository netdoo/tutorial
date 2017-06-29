package com.esagg;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {

        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        TestSuite testSuite =  new TestSuite( AppTest.class );
        testSuite.addTestSuite(FooTest.class);
        return testSuite;
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {

        assertTrue( true );
    }

    public void test2() {
        assertTrue(true);
    }

    public void test_foo() {
        assertTrue(true);
    }
}
