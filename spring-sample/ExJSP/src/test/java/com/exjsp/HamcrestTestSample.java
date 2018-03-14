package com.exjsp;


import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HamcrestTestSample {

    @Test
    public void nullTest() {
        assertThat(null, is(nullValue()));
    }

    @Test
    public void notNullTest() {
        assertThat("aaa", not(nullValue()));
    }

    @Test
    public void instanceOfTest() {
        assertThat(new Long(10), instanceOf(Long.class));
    }

    @Test
    public void numberTest() {
        assertThat(2,  greaterThan(1));
        assertThat(1,  greaterThanOrEqualTo(1));
        assertThat(0,  lessThan(1));
        assertThat(0,  lessThanOrEqualTo(1));
    }

    @Test
    public void stringTest() {
        assertThat("Spring",  equalToIgnoringCase("spring"));
        /// 문자열 앞뒤의 공백을 제거후 비교함
        assertThat("Spring Framework 3.2",  equalToIgnoringWhiteSpace ("   Spring Framework 3.2  "));
        assertThat("Spring Framework 3.2", containsString("Framework"));
    }
}
