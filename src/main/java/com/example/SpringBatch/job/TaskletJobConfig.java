package com.example.SpringBatch.job;

import com.example.SpringBatch.job.step.TaskletStep;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class TaskletJobConfig {
    private final TaskletStep taskletStep;

    @Bean
    public Job taskletJob(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new JobBuilder("taskletJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(taskletStep.videoStatistics(jobRepository, serviceTransactionManager))
                .next(taskletStep.videoCalculate(jobRepository, serviceTransactionManager))
                .next(taskletStep.adStatistics(jobRepository, serviceTransactionManager))
                .next(taskletStep.adCalculate(jobRepository, serviceTransactionManager))
                .build();
    }

}
