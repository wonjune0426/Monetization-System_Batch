package com.example.springbatch.entity.statisitcs;

import com.example.springbatch.entity.Video;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoStatistics{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoStatisticId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @Column(nullable = false)
    private Long videoView;

    @Column(nullable = false)
    private Long videoPlaytime;

    @Column(nullable = false)
    private LocalDate createdAt = LocalDate.now().minusDays(1);


    public VideoStatistics(Video video, Long videoView, Long videoPlaytime) {
        this.video = video;
        this.videoView = videoView;
        this.videoPlaytime = videoPlaytime;
    }
}
