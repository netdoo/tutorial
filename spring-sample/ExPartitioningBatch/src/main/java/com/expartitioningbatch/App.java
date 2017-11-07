package com.expartitioningbatch;

import com.expartitioningbatch.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class App {
    static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        AnnotationConfigApplicationContext context = null;

        try {
            context = new AnnotationConfigApplicationContext(AppConfig.class);
            JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
            Job job = (Job) context.getBean("mainBatchJob");

            try {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("data", "mydata")
                        .addString("date", simpleDateFormat.format(new Date()))
                        .addString("fileName", "/var/test.dat")
                        .toJobParameters();

                JobExecution execution = jobLauncher.run(job, jobParameters);
                logger.info("Exit Status {} ", execution.getStatus());
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
