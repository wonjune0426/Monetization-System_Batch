package com.example.SpringBatch.entity.statisitcs;

import com.example.SpringBatch.entity.Video;
import com.example.SpringBatch.entity.timestapm.CreateTimestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoStatistics extends CreateTimestamped {
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

    public VideoStatistics(Video video, Long videoView, Long videoPlaytime) {
        this.video = video;
        this.videoView = videoView;
        this.videoPlaytime = videoPlaytime;
    }
}
