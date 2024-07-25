package com.example.springbatch.entity;

import com.example.springbatch.entity.timestapm.CreateTimestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoAd extends CreateTimestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoAdId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ad_id")
    private Ad ad;

    @Column(nullable = false)
    private Long totalView;

    @Column(nullable = false)
    private Boolean deleteCheck;
}
