package com.exfastutil;

import it.unimi.dsi.fastutil.longs.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapTest {

    final static Logger logger = LoggerFactory.getLogger(MapTest.class);
    final static int MAX_PUT_COUNT = 100_000;
    final static String dummyValue = StringUtils.leftPad("0", 32, "#");

    @Test
    public void _0_테스트_HashMap() {

        Map<Long, String> map = new HashMap<>();

        for (long i = 0; i < MAX_PUT_COUNT; i++) {
            map.put(i, dummyValue);
        }
    }

    @Test
    public void _1_테스트_ObjectArrayMap() {

        Long2ObjectMap<String> map = new Long2ObjectArrayMap<>();

        for (long i = 0; i < MAX_PUT_COUNT; i++) {
            map.put(i, dummyValue);
        }
    }

    @Test
    public void _2_테스트_AVLTreeMap() {

        Long2ObjectMap<String> map = new Long2ObjectAVLTreeMap<>();

        for (long i = 0; i < MAX_PUT_COUNT; i++) {
            map.put(i, dummyValue);
        }
    }

    @Test
    public void _3_테스트_LinkedOpenHashMap() {

        Long2ObjectMap<String> map = new Long2ObjectLinkedOpenHashMap<>();

        for (long i = 0; i < MAX_PUT_COUNT; i++) {
            map.put(i, dummyValue);
        }
    }

    @Test
    public void _4_테스트_OpenHashMap() {

        Long2ObjectMap<String> map = new Long2ObjectOpenHashMap<>();

        for (long i = 0; i < MAX_PUT_COUNT; i++) {
            map.put(i, dummyValue);
        }
    }


    @Test
    public void _5_테스트_RBTreeMap() {

        Long2ObjectMap<String> map = new Long2ObjectRBTreeMap<>();

        for (long i = 0; i < MAX_PUT_COUNT; i++) {
            map.put(i, dummyValue);
        }
    }




}
