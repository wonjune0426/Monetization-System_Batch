package com.example.SpringBatch.tasklet;

import com.example.SpringBatch.entity.Video;
import com.example.SpringBatch.projection.VideoStatsProjection;
import com.example.SpringBatch.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VideoCalculateTasklet implements Tasklet {

    private final VideoRepository videoRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        // 통계 Step에서 정산 된 값을 가져 옴
        List<VideoStatsProjection> videoStatsProjectionList = (List<VideoStatsProjection>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("videoStatsProjectionList");

        for (VideoStatsProjection videoStatsProjection : videoStatsProjectionList) {
            // video 테이블에서 해당 video객체 조회
            Video video = videoRepository.findById(videoStatsProjection.getVideoId()).orElseThrow(
                    ()-> new RuntimeException("Video not found")
            );

            // 이전 날짜의 누적 조회 수 계산
            Long startView = video.getTotalView() - videoStatsProjection.getVideoView();

        }


        return null;
    }



}
