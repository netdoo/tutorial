package com.exjunit5;

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
public class MinusTest {

    @Parameter(value = 0)
    public int numberA;

    @Parameter(value = 1)
    public int numberB;

    @Parameter(value = 2)
    public int expected;

    @Parameters(name = "{index}: testMinus({0}-{1}) = {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1, 1, 0},
                {2, 2, 0},
                {8, 2, 6},
                {4, 5, -1},
                {7, 5, 2}
        });
    }

    public int minus(int numberA, int numberB) {
        return numberA - numberB;
    }

    @Test
    public void testMinus() {
        assertThat(minus(numberA, numberB), is(expected));
    }
}
