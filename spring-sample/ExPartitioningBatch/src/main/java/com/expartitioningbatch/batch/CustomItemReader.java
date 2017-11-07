package com.expartitioningbatch.batch;

import com.expartitioningbatch.domain.ExecutionContextParam;
import com.expartitioningbatch.repository.AlphabetDB;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

public class CustomItemReader implements ItemReader<List<String>> {

    final Logger logger = LoggerFactory.getLogger(CustomItemReader.class);

    @Autowired
    AlphabetDB alphabetDB;

    final int maxReadCount = 3;
    ExecutionContextParam param;
    List<List<String>> targetGroupList;
    Iterator<List<String>> targetGroupListIterator;

    public CustomItemReader(ExecutionContextParam param) {
        this.param = param;
        targetGroupList = Lists.partition(param.getTargetList(), maxReadCount);
        targetGroupListIterator = targetGroupList.iterator();
    }

    @Override
    public List<String> read() throws Exception, UnexpectedInputException, ParseException {

        if (!targetGroupListIterator.hasNext())
            return null;

        List<String> targetList = targetGroupListIterator.next();
        logger.info("CustomItemReader ThreadId {}, targetList {} ", Thread.currentThread().getId(), targetList);
        return targetList;
    }
}

