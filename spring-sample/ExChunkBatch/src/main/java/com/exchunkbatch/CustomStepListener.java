package com.exchunkbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * Created by Administrator on 2018-02-23.
 */
public class CustomStepListener implements StepExecutionListener {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.info("beforeStep");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("afterStep");
        return null;
    }
}
