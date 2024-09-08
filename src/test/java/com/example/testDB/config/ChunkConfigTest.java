package com.example.testDB.config;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {ChunkConfig.class})
@ContextConfiguration(classes = {ChunkConfig.class})
@TestPropertySource(locations = "classpath:application_test.properties")
class ChunkConfigTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("chunkJob")
    Job job;

    @Test
    public void helloTest() throws Exception {
        jobLauncherTestUtils.setJob(job);
        JobParameters jobParameters = this.jobLauncherTestUtils.getUniqueJobParameters();
        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

}