package com.exchunkbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class PrintTasklet implements Tasklet {
    static final Logger logger = LoggerFactory.getLogger(PrintTasklet.class);
    String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
        logger.info("ThreadId {}, Message {}", Thread.currentThread().getId(), message);
        return RepeatStatus.FINISHED;
    }
}
