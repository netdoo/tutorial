package com.exelasticbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ProcessTasklet implements Tasklet, StepExecutionListener {
    static final Logger logger = LoggerFactory.getLogger(ProcessTasklet.class);
    String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
        this.message = this.message.toUpperCase();
        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.message = stepExecution.getJobExecution().getExecutionContext().getString("message");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution.getJobExecution().getExecutionContext().putString("message", this.message);
        return null;
    }
}
