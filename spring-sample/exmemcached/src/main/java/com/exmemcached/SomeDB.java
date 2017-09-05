package com.exmemcached;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SomeDB {

    List<SomeData> someDataList = new ArrayList<>();

    public SomeDB() {
        someDataList.add(new SomeData(0, "tVN"));
        someDataList.add(new SomeData(1, "mbc"));
        someDataList.add(new SomeData(2, "kbs"));
        someDataList.add(new SomeData(3, "sbs"));
        someDataList.add(new SomeData(4, "ebs"));
        someDataList.add(new SomeData(5, "iMBC"));
        someDataList.add(new SomeData(6, "jTBC"));
    }

    public SomeData find(long id) {
        Optional<SomeData> findData = someDataList.stream().filter(data -> data.getId() == id).findFirst();
        return findData.get();
    }

    public List<SomeData> find(List<Long> ids) {
        List<SomeData> findDatas = someDataList.stream().filter(data -> ids.contains(data.getId())).collect(Collectors.toList());
        return findDatas;
    }
}
