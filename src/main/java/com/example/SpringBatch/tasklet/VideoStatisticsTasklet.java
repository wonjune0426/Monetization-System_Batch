package com.example.SpringBatch.tasklet;

import com.example.SpringBatch.projection.VideoStatsProjection;
import com.example.SpringBatch.entity.statisitcs.VideoStatistics;
import com.example.SpringBatch.repository.VideoRepository;
import com.example.SpringBatch.repository.VideoViewHistoryRepository;
import com.example.SpringBatch.repository.statisitcs.VideoStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class VideoStatisticsTasklet implements Tasklet {

    private final VideoViewHistoryRepository videoViewHistoryRepository;
    private final VideoStatisticsRepository videoStatisticsRepository;
    private final VideoRepository videoRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<VideoStatsProjection> videoStatsProjectionList = videoViewHistoryRepository.findVideoStatsByLocalDate(LocalDate.parse("2024-07-01"));
        for (VideoStatsProjection videoStatsProjection : videoStatsProjectionList) {
            VideoStatistics videoStatistics = new VideoStatistics(
                    videoRepository.findById(videoStatsProjection.getVideoId()).orElseThrow(
                            ()-> new IllegalArgumentException("존재하지 않는 영상입니다")
                    ),
                    videoStatsProjection.getVideoView(),
                    videoStatsProjection.getVideoPlaytime()
            );
            videoStatisticsRepository.save(videoStatistics);
        }

        // 다음 step인 정산에 통계 값 전달
        chunkContext.getStepContext().getStepExecution().getExecutionContext().put("videoStatsProjectionList",videoStatsProjectionList);


        return RepeatStatus.FINISHED;
    }
}
