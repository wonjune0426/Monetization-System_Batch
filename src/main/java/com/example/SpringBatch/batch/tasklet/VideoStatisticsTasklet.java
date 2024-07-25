package com.example.springbatch.batch.tasklet;

import com.example.springbatch.dto.VideoStats;
import com.example.springbatch.entity.statisitcs.VideoStatistics;
import com.example.springbatch.repository.read.Read_VideoRepository;
import com.example.springbatch.repository.read.Read_VideoViewHistoryRepository;
import com.example.springbatch.repository.write.statisitcs.Write_VideoStatisticsRepository;
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

    private final Read_VideoViewHistoryRepository read_videoViewHistoryRepository;
    private final Read_VideoRepository read_videoRepository;
    private final Write_VideoStatisticsRepository write_videoStatisticsRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){

        List<VideoStats> videoStatsList = read_videoViewHistoryRepository.findVideoStatsByLocalDate(LocalDate.now().minusDays(1));
        for (VideoStats videoStats : videoStatsList) {
            VideoStatistics videoStatistics = new VideoStatistics(
                    read_videoRepository.findById(videoStats.getVideoId()).orElseThrow(
                            ()-> new IllegalArgumentException("존재하지 않는 영상입니다")
                    ),
                    videoStats.getVideoView(),
                    videoStats.getVideoPlaytime()
            );
            write_videoStatisticsRepository.save(videoStatistics);
        }

        return RepeatStatus.FINISHED;
    }
}
