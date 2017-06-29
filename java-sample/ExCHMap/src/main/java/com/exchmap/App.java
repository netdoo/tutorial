package com.exchmap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class App {

    List<String> airList = new ArrayList<>(Arrays.asList("MBC", "SBS", "KBS"));
    List<String> cableList = new ArrayList<>(Arrays.asList("TVN", "JTBC", "iMBC", "MBN"));
    List<String> portalList = new ArrayList<>(Arrays.asList("Nate", "DAUM", "Naver", "Yahoo", "AOL", "Google"));

    /// Fail-fast Iterator
    public void concurrentModificationExceptionSample() {
        HashMap<Long, List<String>> map = new HashMap<>();

        map.put(new Long(1), airList);
        map.put(new Long(2), cableList);

        try {
            for (Map.Entry<Long, List<String>> entry : map.entrySet()) {
                System.out.println("CMESample " +  entry.getKey() + ":" + entry.getValue());
                map.put(new Long(3), portalList);
            }
        } catch (Exception e) {
            System.err.println("CMESample " + e.toString());
        }
    }

    public void atomicConcurrentModificationExceptionSample() {
        AtomicReference<HashMap<Long, List<String>>> atomicMap = new AtomicReference<>();

        HashMap<Long, List<String>> map = new HashMap<>();

        map.put(new Long(1), airList);
        map.put(new Long(2), cableList);

        atomicMap.set(map);

        HashMap<Long, List<String>> tempMap = atomicMap.get();

        try {
            for (Map.Entry<Long, List<String>> entry : tempMap.entrySet()) {
                System.out.println("CMESample " +  entry.getKey() + ":" + entry.getValue());
                HashMap<Long, List<String>> newMap = new HashMap<>(map);
                newMap.put(new Long(1), portalList);
                atomicMap.set(newMap);
            }
        } catch (Exception e) {
            System.err.println("CMESample " + e.toString());
        }
    }


    public void mapStreamSample() {
        HashMap<Long, List<String>> map = new HashMap<>();

        map.put(new Long(1), airList);
        map.put(new Long(2), cableList);

        try {
            map.forEach((k,v) -> {
                map.put(new Long(3), portalList);
                System.out.println("MapStreamSample " +  k + " : " + v);
            });
        } catch (Exception e) {
            System.err.println("MapStreamSample " + e.toString());
        }
    }


    /// Fail-safe Iterator

    /**
     * ConcurrentHashMap의 경우 해당 Map의 복사본을 참조하는 Iterator를 반환한다.
     * 반환 받은 시점 이후에 Map의 요소가 변경되었다면 해당 Iterator에는 반영이 되지 않으며
     * ConcurrentModificationException도 발생하지 않는다.
     * 즉 Fail-fast하지 않고 Weakly Consistent(약한 일관성)를 제공한다. 이를 Fail-safe Iterator라고도 부른다.
     */

    public void  weaklyConsistentSample() {
        ConcurrentHashMap<Long, List<String>> map = new ConcurrentHashMap<>();

        map.put(new Long(1), airList);
        map.put(new Long(2), cableList);

        try {
            for (ConcurrentHashMap.Entry<Long, List<String>> entry : map.entrySet()) {
                map.put(new Long(3), portalList);
                System.out.println("WeaklyConsistentSample " + entry.getKey() + ":" + entry.getValue());
            }
        } catch (Exception e) {
            System.err.println("WeaklyConsistentSample " + e.toString());
        }
    }

    public void weaklyConsistentStreamSample() {
        ConcurrentHashMap<Long, List<String>> map = new ConcurrentHashMap<>();

        map.put(new Long(1), airList);
        map.put(new Long(2), cableList);

        try {
            map.forEach((k,v) -> {
                map.put(new Long(3), portalList);
                System.out.println("WeaklyConsistentStream " +  k + " : " + v);
            });
        } catch (Exception e) {
            System.err.println("WeaklyConsistentStream " + e.toString());
        }
    }

    public void simpleListSample() {
        List<String> list = new ArrayList<>();
        list.addAll(airList);

        for (String s : list) {
            System.out.println(s);
            list.addAll(cableList);
        }
    }

    public void simpleAtomicRefListSample() {
        AtomicReference<List<String>> atomicRefList = new AtomicReference<>();

        List<String> dataList = new ArrayList<>();
        dataList.addAll(airList);
        atomicRefList.set(dataList);
        List<String> tempList = atomicRefList.get();

        for (String s : tempList) {
            System.out.println(s);
            atomicRefList.set(cableList);
        }

        List<String> tempList2 = atomicRefList.get();

        for (String s : tempList2) {
            System.out.println(s);
        }
    }

    public void atomicRefSample() {
        AtomicReference<List<String>> list = new AtomicReference<>();

        list.set(airList);
        List<String> aList = list.get();

        for (String s : aList) {
            System.out.println(s);
            list.set(cableList);
        }

    }

    public static void main( String[] args ) {
        App app = new App();
        app.atomicConcurrentModificationExceptionSample();
        app.concurrentModificationExceptionSample();
        app.weaklyConsistentSample();
        app.mapStreamSample();
        app.weaklyConsistentStreamSample();
        app.atomicRefSample();
        app.simpleListSample();
        app.simpleAtomicRefListSample();
        app.simpleAtomicRefListSample();
    }
}
