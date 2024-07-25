package com.example.springbatch.entity;

import com.example.springbatch.entity.timestapm.MainTimestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Video extends MainTimestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID videoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @Column(nullable = false, length = 100)
    private String videoName;

    @Column(nullable = false)
    private String videoDescription;

    @Column(nullable = false)
    private Long videoLength;

    @Column(nullable = false)
    private Long totalView;

    @Column(nullable = false)
    private Boolean deleteCheck;

}
