package com.example.springbatch.entity.statisitcs;


import com.example.springbatch.entity.VideoAd;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdStatistics{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adStatisticId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="video_ad_id")
    private VideoAd videoAd;

    @Column(nullable = false)
    private Long adView;

    @Column(nullable = false)
    private LocalDate createdAt = LocalDate.now().minusDays(1);

    public AdStatistics(VideoAd videoAd, Long adView) {
        this.videoAd = videoAd;
        this.adView = adView;
    }
}
