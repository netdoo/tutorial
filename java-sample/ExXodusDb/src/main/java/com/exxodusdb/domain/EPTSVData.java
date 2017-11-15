package com.exxodusdb.domain;

import java.util.Date;

/**
 * Created by jhkwon78 on 2017-11-15.
 */
public class EPTSVData {
    String id;
    Date date;
    String namedKey;
    String namedValue;
    String title;
    String pcPrice;
    String opClass;

    public EPTSVData(String line, boolean isAllEP) throws Exception {
        String cols[] = line.split("\t");
        this.id = cols[0];
        this.title = cols[1];
        this.pcPrice = cols[2];

        if (isAllEP == false && cols.length > 36) {
            // 요약에만 클래스 타입이 있음.
            this.opClass = cols[36];
        }

        this.namedKey = this.title + "\t" + this.pcPrice;
        this.namedValue = this.id + "\t" + this.title + "\t" + this.pcPrice;

        // 36번째 클래스
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
