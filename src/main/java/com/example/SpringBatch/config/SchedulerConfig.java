package com.example.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final Job videoJob;
    private final Job adJob;
    private final JobLauncher jobLauncher;

    public SchedulerConfig(@Qualifier("VIDEO_JOB") Job videoJob, @Qualifier("AD_JOB") Job adjob, JobLauncher jobLauncher) {
        this.videoJob = videoJob;
        this.adJob = adjob;
        this.jobLauncher = jobLauncher;
    }

    @Scheduled(cron = "0 30 0 * * ? ")
    //@Scheduled(cron = "0 * * * * ?")
    public void jobLauncher() throws Exception {
        jobLauncher.run(videoJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters());
        jobLauncher.run(adJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters());
    }
}
