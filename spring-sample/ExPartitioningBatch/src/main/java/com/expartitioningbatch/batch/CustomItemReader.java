package com.expartitioningbatch.batch;

import com.expartitioningbatch.repository.AlphabetDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CustomItemReader implements ItemReader<List<String>> {

    final Logger logger = LoggerFactory.getLogger(CustomItemReader.class);

    @Autowired
    AlphabetDB alphabetDB;

    private int from;
    private int to;
    boolean readDone;

    @Override
    public List<String> read() throws Exception, UnexpectedInputException, ParseException {

        if (readDone)
            return null;

        logger.info("CustomItemReader ThreadId {}, from {}, to {} ", Thread.currentThread().getId(), from, to);
        List<String> readList = new ArrayList<>();

        for (int i = from; i <= to; i++) {
            readList.add(alphabetDB.get(i));
        }

        readDone = true;
        return readList;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getFrom() {
        return this.from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTo() {
        return this.to;
    }
}

