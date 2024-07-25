package com.example.springbatch.batch.chunk;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChunkJob {

    private final ChunkVideoStep chunkVideoStep;
    private final ChunkAdStep chunkAdStep;

    @Bean(name="VIDEO_JOB")
    public Job videoJob(JobRepository jobRepository) {
        return new JobBuilder("videoJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkVideoStep.videoStatistics(jobRepository))
                .next(chunkVideoStep.videoCalculate(jobRepository))
                .build();
    }

    @Bean(name="AD_JOB")
    public Job adJob(JobRepository jobRepository) {
        return new JobBuilder("adJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkAdStep.adStatistics(jobRepository))
                .next(chunkAdStep.adCalculate(jobRepository))
                .build();
    }
}
