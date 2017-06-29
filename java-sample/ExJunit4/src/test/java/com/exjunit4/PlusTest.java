package com.exjunit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

@RunWith(value = Parameterized.class)
public class PlusTest {

    @Parameter(value = 0)
    public int numberA;

    @Parameter(value = 1)
    public int numberB;

    @Parameter(value = 2)
    public int expected;

    @Parameters(name = "{index}: testPlus({0}+{1}) = {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1, 1, 2},
                {2, 2, 4},
                {8, 2, 10},
                {4, 5, 9},
                {5, 5, 10}
        });
    }

    public int plus(int numberA, int numberB) {
        return numberA + numberB;
    }

    @Test
    public void testPlus() {
        assertThat(plus(numberA, numberB), is(expected));
    }
}
