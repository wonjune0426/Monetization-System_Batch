package com.example.SpringBatch.job;

import com.example.SpringBatch.job.step.ChunkAdStep;
import com.example.SpringBatch.job.step.ChunkVideoStep;
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
public class ChunkJobConfig {

    private final ChunkVideoStep chunkVideoStep;
    private final ChunkAdStep chunkAdStep;

    @Bean("VIDEO_JOB")
    public Job videoJob(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new JobBuilder("videoJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkVideoStep.videoStatistics(jobRepository,serviceTransactionManager))
                .next(chunkVideoStep.videoCalculate(jobRepository,serviceTransactionManager))
                .build();
    }

    @Bean("AD_JOB")
    public Job adJob(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new JobBuilder("adJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkAdStep.adStatistics(jobRepository,serviceTransactionManager))
                .next(chunkAdStep.adCalculate(jobRepository,serviceTransactionManager))
                .build();
    }
}
