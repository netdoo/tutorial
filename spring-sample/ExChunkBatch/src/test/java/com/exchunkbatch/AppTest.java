package com.exchunkbatch;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {
        "classpath:applicationContext.xml",
        "classpath:batch/job.xml",
        "classpath:batch/compositeJob.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class AppTest {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("testBatchJob")
    Job testBatchJob;

    @Autowired
    @Qualifier("myBatchJob")
    Job myBatchJob;

    @Autowired
    @Qualifier("testCompositeBatchJob")
    Job testCompositeBatchJob;

    @Test
    public void testBatchJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("key", "value")
                .toJobParameters();

        jobLauncher.run(testBatchJob, jobParameters);
    }

    @Test
    public void testMyBatchJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("key", "value")
                .toJobParameters();

        jobLauncher.run(myBatchJob, jobParameters);
    }

    @Test
    public void testCompositeBatchJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("key", "value")
                .toJobParameters();

        jobLauncher.run(testCompositeBatchJob, jobParameters);
    }
}
