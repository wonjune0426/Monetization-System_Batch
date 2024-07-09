package com.example.SpringBatch.tasklet;

import com.example.SpringBatch.entity.VideoAd;
import com.example.SpringBatch.entity.calculate.AdCalculate;
import com.example.SpringBatch.entity.statisitcs.AdStatistics;
import com.example.SpringBatch.repository.VideoAdRepository;
import com.example.SpringBatch.repository.calculate.AdCalculateRepository;
import com.example.SpringBatch.repository.statisitcs.AdStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@StepScope
@Component
@RequiredArgsConstructor
public class AdCalculateTasklet implements Tasklet {

    private final AdStatisticsRepository adStatisticsRepository;
    private final VideoAdRepository videoAdRepository;
    private final AdCalculateRepository adCalculateRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<AdStatistics> adStatisticsList = adStatisticsRepository.findAllByCreatedAt(LocalDate.now().minusDays(1));
        for (AdStatistics adStatistics : adStatisticsList) {
            VideoAd videoAd = videoAdRepository.findById(adStatistics.getVideoAd().getVideoAdId()).orElseThrow(
                    ()-> new RuntimeException("Video Ad not found")
            );


            long totalView = adStatistics.getAdView();
            long accumulateView = videoAd.getTotalView() - totalView;

            AdCalculate adCalculate = new AdCalculate(videoAd,calculateAmount(accumulateView,totalView));
            adCalculateRepository.save(adCalculate);
        }

        return RepeatStatus.FINISHED;
    }

    private long calculateAmount(Long accumulateView, Long todayView) {
        long videoAmount = 0L;  // 정산할 금액 저장
        long[] thresholds = {100000L, 500000L, 1000000L}; // 기준 점이 되는 조회 수
        long[] multipliers = {10, 12, 15, 20};  // 각각의 조회 수 마다의 단가

        for (int i = 0; i < thresholds.length; i++) {
            if (accumulateView < thresholds[i]) {
                long checkView = thresholds[i] - accumulateView;
                if (todayView <= checkView) {
                    videoAmount += todayView * multipliers[i];
                    return videoAmount;
                } else {
                    videoAmount += checkView * multipliers[i];
                    todayView -= checkView;
                    accumulateView += checkView;
                }
            }
        }
        // 1,000,000 이상의 경우
        videoAmount += todayView * multipliers[multipliers.length - 1];
        return videoAmount;
    }
}
