package com.expartitioningbatch;

import com.expartitioningbatch.config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@ContextConfiguration(classes = {AppConfig.class})
@ImportResource({"classpath:applicationContext.xml", "classpath:batch/job.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class AppTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("mainBatchJob")
    private Job job;

    static final Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void testLaunchJob() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("data", "mydata")
                .addString("date", simpleDateFormat.format(new Date()))
                .addString("fileName", "/var/test.dat")
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, jobParameters);
        logger.info("Exit Status {} ", execution.getStatus());
    }
}
