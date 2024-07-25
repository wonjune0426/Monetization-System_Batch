package com.example.springbatch.batch.tasklet;

import com.example.springbatch.dto.AdStats;
import com.example.springbatch.entity.statisitcs.AdStatistics;
import com.example.springbatch.repository.read.Read_AdViewHistoryRepository;
import com.example.springbatch.repository.read.Read_VideoAdRepository;
import com.example.springbatch.repository.write.statisitcs.Write_AdStatisticsRepository;
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

    private final Write_AdStatisticsRepository write_adStatisticsRepository;
    private final Read_AdViewHistoryRepository read_adViewHistoryRepository;
    private final Read_VideoAdRepository read_videoAdRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)  {

        List<AdStats> adStatsList = read_adViewHistoryRepository.findAdStatsByLocalDate(LocalDate.now().minusDays(1));
        for (AdStats adStats : adStatsList) {
            AdStatistics adStatistics = new AdStatistics(
                    read_videoAdRepository.findById(adStats.getVideoAdId()).orElseThrow(
                            ()-> new IllegalArgumentException("존재 하지 않는 VideoAd 입니다.")
                    ), adStats.getAdView()
            );
            write_adStatisticsRepository.save(adStatistics);
        }

        return RepeatStatus.FINISHED;
    }
}
