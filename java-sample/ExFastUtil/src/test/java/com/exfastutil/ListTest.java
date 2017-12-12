package com.exfastutil;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListTest {

    final static Logger logger = LoggerFactory.getLogger(ListTest.class);
    final static int MAX_PUT_COUNT = 100_000;
    final static String dummyStringValue = StringUtils.leftPad("0", 32, "#");

    @Test
    public void _0_테스트_ArrayList() {

        List<String> list = new ArrayList<>();

        for (long i = 0; i < MAX_PUT_COUNT; i++) {
            list.add(dummyStringValue);
        }
    }

    @Test
    public void _1_테스트_ObjectArrayList() {

        ObjectArrayList<String> list = new ObjectArrayList<>();

        for (long i = 0; i < MAX_PUT_COUNT; i++) {
            list.add(dummyStringValue);
        }
    }
}
