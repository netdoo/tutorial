package com.exflowbatch;


import com.exflowbatch.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        AnnotationConfigApplicationContext context = null;

        try {
            context = new AnnotationConfigApplicationContext(AppConfig.class);
            JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
            Job myBatchJob = (Job) context.getBean("myBatchJob");
            Job myFlowBatchJob = (Job) context.getBean("myFlowBatchJob");

            try {
                JobExecution myBatchJobExecution = jobLauncher.run(myBatchJob, new JobParameters());
                JobExecution myFlowBatchJobExection = jobLauncher.run(myFlowBatchJob, new JobParameters());
                logger.info("Exit Status myBatchJob {}, myFlowBatchJob {} ", myBatchJobExecution.getStatus(), myFlowBatchJobExection.getStatus());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            if (null != context) {
                context.close();
            }
            logger.info("Done");
        }
    }
}

