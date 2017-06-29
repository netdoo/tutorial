package com.exchunkbatch;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class CustomItemReader implements ItemReader<String> {

    private List<String> alphabetList;
    private int count = 0;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException {
        if(count < alphabetList.size()){
            return alphabetList.get(count++);
        }else{
            return null;
        }
    }
    public List<String> getAlphabetList() {
        return alphabetList;
    }
    public void setAlphabetList(List<String> alphabetList) {
        this.alphabetList = alphabetList;
    }
}

