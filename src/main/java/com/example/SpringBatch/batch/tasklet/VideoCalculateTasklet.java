package com.example.springbatch.batch.tasklet;

import com.example.springbatch.entity.Video;
import com.example.springbatch.entity.calculate.VideoCalculate;
import com.example.springbatch.entity.statisitcs.VideoStatistics;
import com.example.springbatch.repository.read.Read_VideoRepository;
import com.example.springbatch.repository.read.Read_VideoStatisticsRepository;
import com.example.springbatch.repository.write.calculate.Write_VideoCalculateRepository;
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
public class VideoCalculateTasklet implements Tasklet {

    private final Read_VideoRepository read_videoRepository;
    private final Read_VideoStatisticsRepository read_videoStatisticsRepository;
    private final Write_VideoCalculateRepository write_videoCalculateRepository;


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        List<VideoStatistics> videoStatisticsList = read_videoStatisticsRepository.findAllByCreatedAt(LocalDate.now().minusDays(1));

        for (VideoStatistics videoStatistics : videoStatisticsList) {

            // 해당 video 존재 여부 확인
            Video video = read_videoRepository.findById(videoStatistics.getVideo().getVideoId()).orElseThrow(
                    ()-> new RuntimeException("Video not found")
            );

            // 당일 조회 수
            long todayView = videoStatistics.getVideoView();

            // 전날 까지의 누적 조회 수
            long accumulateView = video.getTotalView() - todayView;


            VideoCalculate videoCalculate = new VideoCalculate(video,calculateAmount(accumulateView,todayView));
            write_videoCalculateRepository.save(videoCalculate);
        }


        return RepeatStatus.FINISHED;
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
