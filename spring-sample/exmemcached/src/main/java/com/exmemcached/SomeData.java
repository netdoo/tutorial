package com.exmemcached;

import com.google.code.ssm.api.CacheKeyMethod;

import java.io.Serializable;

/// memcache를 할 객체는 반드시 Serializable 를 구현해야 함.
public class SomeData implements Serializable {
    long id;
    String name;

    public SomeData() {
    }

    public SomeData(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @CacheKeyMethod
    public String getCacheKey() {
        return String.valueOf(this.id);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
