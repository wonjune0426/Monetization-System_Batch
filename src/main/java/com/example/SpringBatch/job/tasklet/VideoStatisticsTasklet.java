package com.example.SpringBatch.job.tasklet;

import com.example.SpringBatch.entity.statisitcs.VideoStatistics;
import com.example.SpringBatch.dto.VideoStats;
import com.example.SpringBatch.repository.VideoRepository;
import com.example.SpringBatch.repository.VideoViewHistoryRepository;
import com.example.SpringBatch.repository.statisitcs.VideoStatisticsRepository;
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
@RequiredArgsConstructor
@Component
public class VideoStatisticsTasklet implements Tasklet {

    private final VideoViewHistoryRepository videoViewHistoryRepository;
    private final VideoStatisticsRepository videoStatisticsRepository;
    private final VideoRepository videoRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<VideoStats> videoStatsList = videoViewHistoryRepository.findVideoStatsByLocalDate(LocalDate.now().minusDays(1));
        for (VideoStats videoStats : videoStatsList) {
            VideoStatistics videoStatistics = new VideoStatistics(
                    videoRepository.findById(videoStats.getVideoId()).orElseThrow(
                            ()-> new IllegalArgumentException("존재하지 않는 영상입니다")
                    ),
                    videoStats.getVideoView(),
                    videoStats.getVideoPlaytime()
            );
            videoStatisticsRepository.save(videoStatistics);
        }

        return RepeatStatus.FINISHED;
    }
}
