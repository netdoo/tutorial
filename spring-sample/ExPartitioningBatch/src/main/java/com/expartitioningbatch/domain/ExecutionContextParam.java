package com.expartitioningbatch.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ExecutionContextParam implements Serializable {

    String name;
    ArrayList<String> targetList;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setTargetList(ArrayList<String> targetList) {
        this.targetList = targetList;
    }

    public ArrayList<String> getTargetList() {
        return this.targetList;
    }
}
