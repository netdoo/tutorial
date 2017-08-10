package com.exjpa.domain;

import javax.persistence.*;

@Entity
@Table(name="tbl_memo")
public class Memo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    Long id;

    @Column(name="memo")
    String memo;

    public Memo() {

    }

    public Memo(String memo) {
        this.memo = memo;
    }

    public void setId(Long Id) {
        this.id = Id;
    }

    public Long getId() {
        return this.id;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return this.memo;
    }


}
