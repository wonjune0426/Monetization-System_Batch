package com.example.springbatch.batch.chunk;

import com.example.springbatch.dto.VideoStats;
import com.example.springbatch.entity.Video;
import com.example.springbatch.entity.calculate.VideoCalculate;
import com.example.springbatch.entity.statisitcs.VideoStatistics;
import com.example.springbatch.repository.read.Read_VideoRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ChunkVideoStep {

    private final EntityManagerFactory writeEntityManagerFactory;
    private final EntityManagerFactory readEntityManagerFactory;

    private final Read_VideoRepository read_videoRepository;

    @Autowired
    @Qualifier("writeTransactionManager")
    private final PlatformTransactionManager writeTransactionManager;

    @JobScope
    public Step videoStatistics(JobRepository jobRepository) {
        return new StepBuilder("videoStatistics", jobRepository)
                .<VideoStats, VideoStatistics>chunk(5000, writeTransactionManager)
                .reader(videoStatisticsItemReader())
                .processor(videoStatisticsItemProcessor())
                .writer(videoStatisticsItemWriter())
                .build();
    }

    @StepScope
    public JpaPagingItemReader<VideoStats> videoStatisticsItemReader() {
        String queryString = String.format("SELECT NEW %s(v.video.videoId as videoId, count(v.videoViewId) as videoView, sum(v.watchTime) as videoPlaytime) " +
                "FROM VideoViewHistory v " +
                "WHERE v.createdAt = :findDate "+
                "GROUP BY v.video.videoId",VideoStats.class.getName());
        return new JpaPagingItemReaderBuilder<VideoStats>()
                .name("videoStatisticsItemReader")
                .entityManagerFactory(readEntityManagerFactory)
                .queryString(queryString)
                .parameterValues(Collections.singletonMap("findDate", LocalDate.of(2024, 8,27)))
                .pageSize(5000)
                .build();
    }

    @StepScope
    public ItemProcessor<VideoStats, VideoStatistics> videoStatisticsItemProcessor() {
        return item -> {
            Video video = read_videoRepository.findById(item.getVideoId()).orElseThrow(
                    ()-> new IllegalArgumentException("Video not found")
            );
            return new VideoStatistics(video,item.getVideoView(),item.getVideoPlaytime());
        };
    }

    @StepScope
    public JpaItemWriter<VideoStatistics> videoStatisticsItemWriter() {
        JpaItemWriter<VideoStatistics> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(writeEntityManagerFactory);
        return jpaItemWriter;
    }


    @JobScope
    public Step videoCalculate(JobRepository jobRepository) {
        return new StepBuilder("videoCalculate",jobRepository)
                .<VideoStatistics, VideoCalculate>chunk(5000,writeTransactionManager)
                .reader(videoCalculateItemReader())
                .processor(videoCalculateItemProcessor())
                .writer(videoCalculateItemWriter())
                .build();
    }

    @StepScope
    public JpaPagingItemReader<VideoStatistics> videoCalculateItemReader() {
        return new JpaPagingItemReaderBuilder<VideoStatistics>()
                .name("videoCalculateItemReader")
                .entityManagerFactory(readEntityManagerFactory)
                .queryString("SELECT v FROM VideoStatistics v WHERE v.createdAt = :findDate")
                .parameterValues(Collections.singletonMap("findDate",LocalDate.of(2024, 8,27)))
                .pageSize(5000)
                .build();

    }

    @StepScope
    public ItemProcessor<VideoStatistics, VideoCalculate> videoCalculateItemProcessor() {
        return item ->{
            Video video = item.getVideo();
            Long accumulateView = video.getTotalView() - item.getVideoView();
            Long videoAmount = calculateAmount(accumulateView,item.getVideoView());
            return new VideoCalculate(video,videoAmount);
        };
    }

    @StepScope
    public JpaItemWriter<VideoCalculate> videoCalculateItemWriter() {
        JpaItemWriter<VideoCalculate> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(writeEntityManagerFactory);
        return jpaItemWriter;
    }

    private long calculateAmount(Long accumulateView, Long todayView) {
        double videoAmount = 0.0;  // 정산할 금액 저장
        long[] thresholds = {100000L, 500000L, 1000000L}; // 기준 점이 되는 조회 수
        double[] multipliers = {10, 12, 15, 20};   // 각각의 조회 수 마다의 단가

        for (int i = 0; i < thresholds.length; i++) {
            if (accumulateView < thresholds[i]) {
                long checkView = thresholds[i] - accumulateView;
                if (todayView <= checkView) {
                    videoAmount += todayView * multipliers[i];
                    return (long)videoAmount;
                } else {
                    videoAmount += checkView * multipliers[i];
                    todayView -= checkView;
                    accumulateView += checkView;
                }
            }
        }
        // 1,000,000 이상의 경우
        videoAmount += todayView * multipliers[multipliers.length - 1];
        return (long)videoAmount;
    }

}
