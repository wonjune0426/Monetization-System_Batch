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
public class Ad extends MainTimestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID adId;

    @Column(nullable = false, length = 100)
    private String adName;

    @Column(nullable = false)
    private String adDescription;

    @Column(nullable = false)
    private Boolean deleteCheck;
}
