package com.example.springbatch.batch.chunk;

import com.example.springbatch.dto.AdStats;
import com.example.springbatch.entity.VideoAd;
import com.example.springbatch.entity.calculate.AdCalculate;
import com.example.springbatch.entity.statisitcs.AdStatistics;
import com.example.springbatch.repository.read.Read_VideoAdRepository;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ChunkAdStep {

    private final EntityManagerFactory readEntityManagerFactory;
    private final EntityManagerFactory writeEntityManagerFactory;

    private final Read_VideoAdRepository read_videoAdRepository;
    private final PlatformTransactionManager writeTransactionManager;

    @JobScope
    public Step adStatistics(JobRepository jobRepository){
        return new StepBuilder("adStatistics",jobRepository)
                .<AdStats, AdStatistics>chunk(10,writeTransactionManager)
                .reader(adStatisticsJpaPagingItemReader())
                .processor(adStatisticsItemProcessor())
                .writer(adStatisticsJpaItemWriter())
                .build();
    }

    @StepScope
    public JpaPagingItemReader<AdStats> adStatisticsJpaPagingItemReader(){
        String queryString = String.format("SELECT NEW %s(v.videoAd.videoAdId as videoAdId, count(v.adviewId) as adView) " +
                "FROM AdViewHistory v " +
                "WHERE v.createdAt = :findDate "+
                "GROUP BY v.videoAd.videoAdId", AdStats.class.getName());
        return new JpaPagingItemReaderBuilder<AdStats>()
                .name("adStatsJpaPagingItemReader")
                .entityManagerFactory(readEntityManagerFactory)
                .queryString(queryString)
                .parameterValues(Collections.singletonMap("findDate", LocalDate.now().minusDays(1)))
                .pageSize(10)
                .build();
    }

    @StepScope
    public ItemProcessor<AdStats, AdStatistics> adStatisticsItemProcessor(){
        return item -> {
            VideoAd videoAd = read_videoAdRepository.findById(item.getVideoAdId()).orElseThrow(
                    ()->new IllegalArgumentException("VideoAd not found")
            );
            return new AdStatistics(videoAd, item.getAdView());
        };
    }

    @StepScope
    public JpaItemWriter<AdStatistics> adStatisticsJpaItemWriter(){
        JpaItemWriter<AdStatistics> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(writeEntityManagerFactory);
        return jpaItemWriter;
    }


    @JobScope
    public Step adCalculate(JobRepository jobRepository){
        return new StepBuilder("adCalculate",jobRepository)
                .<AdStatistics, AdCalculate>chunk(10,writeTransactionManager)
                .reader(adCalculateJpaPagingItemReader())
                .processor(adCalculateItemProcessor())
                .writer(adCalculateJpaItemWriter())
                .build();
    }

    @JobScope
    public JpaPagingItemReader<AdStatistics> adCalculateJpaPagingItemReader(){
        return new JpaPagingItemReaderBuilder<AdStatistics>()
                .name("adCalculateJpaPagingItemReader")
                .entityManagerFactory(readEntityManagerFactory)
                .queryString("SELECT v FROM AdStatistics v WHERE v.createdAt = :findDate")
                .parameterValues(Collections.singletonMap("findDate",LocalDate.now().minusDays(1)))
                .pageSize(10)
                .build();
    }

    @JobScope
    public ItemProcessor<AdStatistics,AdCalculate> adCalculateItemProcessor(){
        return item -> {
            VideoAd videoAd = item.getVideoAd();
            Long accumulateView = videoAd.getTotalView() - item.getAdView();
            Long adMount = calculateAmount(accumulateView, item.getAdView());
            return new AdCalculate(videoAd,adMount);
        };
    }

    @JobScope
    public JpaItemWriter<AdCalculate> adCalculateJpaItemWriter(){
        JpaItemWriter<AdCalculate> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(writeEntityManagerFactory);
        return jpaItemWriter;
    }

    private long calculateAmount(Long accumulateView, Long todayView) {
        double videoAmount = 0.0;  // 정산할 금액 저장
        long[] thresholds = {100000L, 500000L, 1000000L}; // 기준 점이 되는 조회 수
        double[] multipliers = {1.0, 1.1, 1.3, 1.5};   // 각각의 조회 수 마다의 단가

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
