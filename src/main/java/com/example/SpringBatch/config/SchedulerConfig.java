package com.example.springbatch.config;

import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableScheduling
public class SchedulerConfig {

    private final Job videoJob;
    private final Job adJob;
    private final JobLauncher jobLauncher;

    public SchedulerConfig(@Qualifier("VIDEO_JOB") Job videoJob, @Qualifier("AD_JOB") Job adjob, JobLauncher jobLauncher) {
        this.videoJob = videoJob;
        this.adJob = adjob;
        this.jobLauncher = jobLauncher;
    }

//    @Scheduled(cron = "0 30 0 * * ? ")
//    @Scheduled(cron = "0 * * * * ?")
    @PostConstruct
    public void jobLauncher() throws Exception {
        long beforeTime = System.currentTimeMillis();
        jobLauncher.run(videoJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters());
        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime)/1000;
        System.out.println("Video Job 경과시간: " + secDiffTime);

        long beforeTime2 = System.currentTimeMillis();
        jobLauncher.run(adJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters());
        long afterTime2 = System.currentTimeMillis();
        System.out.println("AD Job 경과시간: "+ (afterTime2 - beforeTime2)/1000);

    }
}
