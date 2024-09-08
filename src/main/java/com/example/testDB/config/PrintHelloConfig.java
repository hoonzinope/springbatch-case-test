package com.example.testDB.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class PrintHelloConfig extends DefaultBatchConfigurer {
    /*
    @Override
    @Bean
    public JobRepository getJobRepository() {
        return super.getJobRepository();
    }

    @Override
    @Bean
    public PlatformTransactionManager getTransactionManager() {
        return super.getTransactionManager();
    }
    */
    @Bean(name = "helloJob")
    public Job printHelloJob() {
        return new JobBuilder("helloJob")
                .repository(getJobRepository())
                .start(printHelloStep(getJobRepository(), getTransactionManager()))
                .build();
    }

    @Bean
    @JobScope
    public Step printHelloStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("helloStep")
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .tasklet(helloTask())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet helloTask() {
        return (stepContribution, chunkContext) -> {
            System.out.println("hello!");
            return RepeatStatus.FINISHED;
        };
    }

}
