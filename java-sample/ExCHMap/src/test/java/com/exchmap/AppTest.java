package com.exchmap;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class AppTest {
    @Test
    public void appTest() {
        Map<String, String> map = new HashMap<>();
        map.put("001", "RED");
        map.put("002", "GREEN");
        map.put("003", "BLUE");
        assertThat(map.getOrDefault("004", "empty"), is("empty"));
    }
}
