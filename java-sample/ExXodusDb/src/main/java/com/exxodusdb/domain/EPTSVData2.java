package com.exxodusdb.domain;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jhkwon78 on 2017-11-15.
 */
public class EPTSVData2 {
    String id;
    Date date;
    String namedKey;
    String namedValue;
    String title;
    String pcPrice;
    String opClass;
    String titleTerm[];
    Map<String, String> titleTermMap = new HashMap<>();

    public EPTSVData2(String line, boolean isAllEP) throws Exception {
        String cols[] = line.split("\t");
        this.id = cols[0];
        this.title = cols[1];
        this.titleTerm = this.title.split(" ");
        this.pcPrice = cols[2];

        for (String term : this.titleTerm) {
            this.titleTermMap.put(term, "");
        }

        for (String term : this.titleTerm) {
            this.titleTermMap.get(term);
        }

        if (isAllEP == false && cols.length > 35) {
            // 요약에만 클래스 타입이 있음.
            this.opClass = cols[35];
        }

        this.namedKey = this.title + "." + this.pcPrice;
        this.namedValue = line;
    }

    public void setNamedKey(String namedKey) {
        this.namedKey = namedKey;
    }

    public String getNamedKey() {
        return this.namedKey;
    }

    public void setNamedValue(String namedValue) {
        this.namedValue = namedValue;
    }

    public String getNamedValue() {
        return this.namedValue;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setPcPrice(String pcPrice) {
        this.pcPrice = pcPrice;
    }

    public String getPcPrice() {
        return this.pcPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return this.date;
    }

    public void setTime(long time) {
        this.date.setTime(time);
    }

    public long getTime() {
        return this.date.getTime();
    }

    public void setOpClass(String opClass) {
        this.opClass = opClass;
    }

    public String getOpClass() {
        return this.opClass;
    }
}

