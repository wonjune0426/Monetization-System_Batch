package com.example.springbatch.batch.tasklet;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class TaskletJob {
    private final TaskletStep taskletStep;

    @Bean
    public Job tasklet_Job(JobRepository jobRepository, PlatformTransactionManager writePlatformTransactionManager) {
        return new JobBuilder("taskletJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(taskletStep.videoStatistics(jobRepository, writePlatformTransactionManager))
                .next(taskletStep.videoCalculate(jobRepository, writePlatformTransactionManager))
                .next(taskletStep.adStatistics(jobRepository, writePlatformTransactionManager))
                .next(taskletStep.adCalculate(jobRepository, writePlatformTransactionManager))
                .build();
    }

}
