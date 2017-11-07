package com.exchunkbatch;

import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class CustomItemReader implements ItemReader<String>, StepExecutionListener {

    private List<String> alphabetList;
    private int count = 0;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException {
        if(count < alphabetList.size()){
            return alphabetList.get(count++);
        } else {
            return null;
        }
    }
    public List<String> getAlphabetList() {
        return alphabetList;
    }
    public void setAlphabetList(List<String> alphabetList) {
        this.alphabetList = alphabetList;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 100_000) {
            sb.append("Hello Batch 2018");
        }

        stepExecution.getJobExecution().getExecutionContext().putString("message", sb.toString());
        return null;
    }
}

