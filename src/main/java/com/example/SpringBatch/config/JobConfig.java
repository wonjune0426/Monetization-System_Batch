package com.example.SpringBatch.config;

import com.example.SpringBatch.tasklet.AdCalculateTasklet;
import com.example.SpringBatch.tasklet.AdStatisticsTasklet;
import com.example.SpringBatch.tasklet.VideoCalculateTasklet;
import com.example.SpringBatch.tasklet.VideoStatisticsTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobConfig {
    private final VideoStatisticsTasklet videoStatisticsTasklet;
    private final VideoCalculateTasklet videoCalculateTasklet;
    private final AdStatisticsTasklet adStatisticsTasklet;
    private final AdCalculateTasklet adCalculateTasklet;

    @Bean
    public Job myJob(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new JobBuilder("myJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(videoStatistics(jobRepository, serviceTransactionManager))
                .next(videoCalculate(jobRepository,serviceTransactionManager))
                .next(adStatistics(jobRepository,serviceTransactionManager))
                .next(adCalculate(jobRepository,serviceTransactionManager))
                .build();
    }




    @JobScope
    @Bean
    public Step videoStatistics(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new StepBuilder("videoStatistics", jobRepository)
                .tasklet(videoStatisticsTasklet,serviceTransactionManager)
                .build();
    }


    @JobScope
    @Bean
    public Step videoCalculate(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new StepBuilder("videoCalculate", jobRepository)
                .tasklet(videoCalculateTasklet,serviceTransactionManager)
                .build();
    }


    @JobScope
    @Bean
    public Step adStatistics(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new StepBuilder("adStatistics",jobRepository)
                .tasklet(adStatisticsTasklet,serviceTransactionManager)
                .build();
    }


    @JobScope
    @Bean
    public Step adCalculate(JobRepository jobRepository,@Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new StepBuilder("adCalculate",jobRepository)
                .tasklet(adCalculateTasklet,serviceTransactionManager)
                .build();
    }


}
