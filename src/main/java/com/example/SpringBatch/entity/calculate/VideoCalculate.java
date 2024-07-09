package com.example.SpringBatch.entity.calculate;

import com.example.SpringBatch.entity.Video;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoCalculate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long videoCalculateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @Column(nullable = false)
    private Long videoAmount;

    @Column(nullable = false)
    private LocalDate createdAt = LocalDate.now().minusDays(1);

    public VideoCalculate(Video video, Long videoAmount) {
        this.video = video;
        this.videoAmount = videoAmount;
    }
}
