package com.example.SpringBatch.job.tasklet;

import com.example.SpringBatch.entity.statisitcs.AdStatistics;
import com.example.SpringBatch.dto.AdStats;
import com.example.SpringBatch.repository.AdViewHistoryRepository;
import com.example.SpringBatch.repository.VideoAdRepository;
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
public class AdStatisticsTasklet implements Tasklet {

    private final AdStatisticsRepository adStatisticsRepository;
    private final AdViewHistoryRepository adViewHistoryRepository;
    private final VideoAdRepository videoAdRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<AdStats> adStatsList = adViewHistoryRepository.findAdStatsByLocalDate(LocalDate.now().minusDays(1));
        for (AdStats adStats : adStatsList) {
            AdStatistics adStatistics = new AdStatistics(
                    videoAdRepository.findById(adStats.getVideoAdId()).orElseThrow(
                            ()-> new IllegalArgumentException("존재 하지 않는 VideoAd 입니다.")
                    ), adStats.getAdView()
            );
            adStatisticsRepository.save(adStatistics);
        }

        return null;
    }
}
