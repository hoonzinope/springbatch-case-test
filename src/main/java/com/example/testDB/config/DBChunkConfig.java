package com.example.testDB.config;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@RequiredArgsConstructor
public class DBChunkConfig extends DefaultBatchConfigurer{

    private final int chunkSize = 3;

    private final DataSource dataSource;
    private final ApplicationContext applicationContext;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:sqlMap/**/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name="dbChunkJob")
    public Job dbChunkJob() throws Exception {
        return new JobBuilder("chunkJobTest")
                .repository(getJobRepository())
                .start(dbChunkStep(getJobRepository(), getTransactionManager()))
                .build();
    }

    @Bean
    @JobScope
    public Step dbChunkStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("chunkStepTest")
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .<Map<String, Object>, String>chunk(chunkSize)
                .reader(dbreader())
                .processor(dbprocessor())
                .writer(mydbwriter())
                .build();
    }

    @Bean
    @StepScope
    public MyBatisPagingItemReader<Map<String, Object>> dbreader() throws Exception {
        return new MyBatisPagingItemReaderBuilder<Map<String, Object>>()
                .pageSize(chunkSize)
                .sqlSessionFactory(sqlSessionFactory())
                .queryId("com.example.testDB.mapper.PlainTextMapper.getPaging")
                //.parameterValues(parameterValues)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Map<String, Object>, String> dbprocessor() {
        return new ItemProcessor<Map<String, Object>, String>() {
            @Override
            public String process(Map<String, Object> item) throws Exception {
                return item.containsKey("text") ? item.get("text").toString().toUpperCase() : "";
            }
        };
    }

    @Bean
    @StepScope
    public ItemWriter<String> dbwriter() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                list.forEach(System.out::println);
            }
        };
    }

    @Bean
    @StepScope
    public MyBatisBatchItemWriter<String> mydbwriter() throws Exception {
        return new MyBatisBatchItemWriterBuilder<String>()
                .sqlSessionFactory(sqlSessionFactory())
                .statementId("com.example.testDB.mapper.ResultTextMapper.putText")
                .build();
    }
}
