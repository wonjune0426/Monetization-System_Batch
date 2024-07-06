package com.example.SpringBatch.config;

import com.example.SpringBatch.tasklet.AdCalculateTasklet;
import com.example.SpringBatch.tasklet.AdStatisticsTasklet;
import com.example.SpringBatch.tasklet.VideoCalculateTasklet;
import com.example.SpringBatch.tasklet.VideoStatisticsTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
    private final AdStatisticsTasklet adStatisticsTasklet;
    private final VideoCalculateTasklet videoCalculateTasklet;
    private final AdCalculateTasklet adCalculateTasklet;

    @Bean
    public Job myJob(JobRepository jobRepository,@Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new JobBuilder("myJob", jobRepository)
                .start(statistics(jobRepository,serviceTransactionManager))
                .next(calculate(jobRepository,serviceTransactionManager))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step statistics(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new StepBuilder("statistics",jobRepository)
                .tasklet(videoStatisticsTasklet,serviceTransactionManager)
                .tasklet(adStatisticsTasklet,serviceTransactionManager)
                .build();
    }

    @Bean
    public Step calculate(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new StepBuilder("calculate",jobRepository)
                .tasklet(videoCalculateTasklet,serviceTransactionManager)
                .tasklet(adCalculateTasklet,serviceTransactionManager)
                .build();
    }




}
