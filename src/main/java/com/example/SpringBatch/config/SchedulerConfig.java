package com.example.SpringBatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerConfig {

    private final Job myJob;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0 30 0 * * ? ")
    public void jobLauncher() throws Exception {
        jobLauncher.run(myJob,new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters());
    }
}
