package com.excollection;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapSortTest {
    final static Logger logger = LoggerFactory.getLogger(MapSortTest.class);

    static <K,V extends Comparable<? super V>>
    List<Map.Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Map.Entry<K,V>> sortedEntries = new ArrayList<>(map.entrySet());

        Collections.sort(sortedEntries,(e1, e2) -> {
                        return e2.getValue().compareTo(e1.getValue());
                    }
        );

        return sortedEntries;
    }

    @Test
    public void _0_테스트_준비() throws Exception {

        Map<String, Integer> map = new HashMap<String, Integer>();

        map.put("A", 34);
        map.put("B", 25);
        map.put("C", 50);
        map.put("D", 50); // "duplicate" value

        List<Map.Entry<String, Integer>> sortList = entriesSortedByValues(map);
        List<Map.Entry<String, Integer>> sortSubList = sortList.subList(0, 3);

        logger.info("{}", sortList);
        logger.info("{}", sortSubList);
    }
}
