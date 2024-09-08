package com.example.testDB.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class ChunkConfig extends DefaultBatchConfigurer{

    @Bean(name="chunkJob")
    public Job chunkJob() {
        return new JobBuilder("chunkJobTest")
                .repository(getJobRepository())
                .start(chunkStep(getJobRepository(), getTransactionManager()))
                .build();
    }

    @Bean
    @JobScope
    public Step chunkStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("chunkStepTest")
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .<String, String>chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<String> reader() {
        return new ListItemReader<>(new ArrayList<>(Arrays.asList("apple","banana","carrot")));
    }

    @Bean
    @StepScope
    public ItemProcessor<String, String> processor() {
        return new ItemProcessor<String, String>() {
            @Override
            public String process(String s) throws Exception {
                return s.toUpperCase();
            }
        };
    }

    @Bean
    @StepScope
    public ItemWriter<String> writer() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                list.forEach(System.out::println);
            }
        };
    }
}
