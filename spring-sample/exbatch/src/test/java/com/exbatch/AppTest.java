package com.exbatch;

import com.exbatch.config.AppConfig;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes = {
    AppConfig.class
},
loader = AnnotationConfigContextLoader.class)
public class AppTest {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("sampleJob")
    Job sampleJob;

    @Test
    public void testApp() throws Exception {
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addString("testData", "erosian mbc sbs");
        JobExecution jobExecution = jobLauncher.run(sampleJob, parametersBuilder.toJobParameters());
        Assert.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }
}
