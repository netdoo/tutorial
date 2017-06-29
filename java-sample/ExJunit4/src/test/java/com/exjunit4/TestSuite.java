package com.exjunit4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({PlusTest.class, MinusTest.class, ToUpperCaseTest.class})
public class TestSuite {
}
