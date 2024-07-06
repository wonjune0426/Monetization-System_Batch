package com.example.SpringBatch.entity.statisitcs;

import com.example.SpringBatch.entity.VideoAd;
import com.example.SpringBatch.entity.timestapm.CreateTimestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdStatistics extends CreateTimestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adStatisticId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="video_ad_id")
    private VideoAd videoAd;

    @Column(nullable = false)
    private Long adView;

    public AdStatistics(VideoAd videoAd, Long adView) {
        this.videoAd = videoAd;
        this.adView = adView;
    }
}
