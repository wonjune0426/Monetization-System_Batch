package com.example.springbatch.batch.tasklet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@RequiredArgsConstructor
public class TaskletStep {
    private final VideoStatisticsTasklet videoStatisticsTasklet;
    private final VideoCalculateTasklet videoCalculateTasklet;
    private final AdStatisticsTasklet adStatisticsTasklet;
    private final AdCalculateTasklet adCalculateTasklet;


    @JobScope
    public Step videoStatistics(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new StepBuilder("videoStatistics", jobRepository)
                .tasklet(videoStatisticsTasklet, serviceTransactionManager)
                .build();
    }


    @JobScope
    public Step videoCalculate(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new StepBuilder("videoCalculate", jobRepository)
                .tasklet(videoCalculateTasklet, serviceTransactionManager)
                .build();
    }


    @JobScope
    public Step adStatistics(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new StepBuilder("adStatistics", jobRepository)
                .tasklet(adStatisticsTasklet, serviceTransactionManager)
                .build();
    }


    @JobScope
    public Step adCalculate(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new StepBuilder("adCalculate", jobRepository)
                .tasklet(adCalculateTasklet, serviceTransactionManager)
                .build();
    }

}
