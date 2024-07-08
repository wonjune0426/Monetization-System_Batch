package com.example.SpringBatch.tasklet;

import com.example.SpringBatch.entity.statisitcs.AdStatistics;
import com.example.SpringBatch.projection.AdStatsProjection;
import com.example.SpringBatch.repository.AdViewHistoryRepository;
import com.example.SpringBatch.repository.VideoAdRepository;
import com.example.SpringBatch.repository.statisitcs.AdStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdStatisticsTasklet implements Tasklet {

    private final AdStatisticsRepository adStatisticsRepository;
    private final AdViewHistoryRepository adViewHistoryRepository;
    private final VideoAdRepository videoAdRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<AdStatsProjection> adStatsProjectionList = adViewHistoryRepository.findAdStatsByLocalDate(LocalDate.now());
        for (AdStatsProjection adStatsProjection : adStatsProjectionList) {
            AdStatistics adStatistics = new AdStatistics(
                    videoAdRepository.findById(adStatsProjection.getVideoAdId()).orElseThrow(
                            ()-> new IllegalArgumentException("존재 하지 않는 VideoAd 입니다.")
                    ),adStatsProjection.getAdView()
            );
            adStatisticsRepository.save(adStatistics);
        }

        return null;
    }
}
