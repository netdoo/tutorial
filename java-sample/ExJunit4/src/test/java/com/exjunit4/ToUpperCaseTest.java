package com.exjunit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(value = Parameterized.class)
public class ToUpperCaseTest {

    @Parameter(value = 0)
    public String param;

    @Parameter(value = 1)
    public String expected;

    @Parameters(name = "{index}: testUpperCaseTest({0}) = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"aaa", "AAA"},
                {"aBc", "ABC"}
        });
    }

    public String toUpperCase(String param) {
        return param.toUpperCase();
    }

    @Test
    public void testPlus() {
        assertThat(toUpperCase(param), is(expected));
    }
}
