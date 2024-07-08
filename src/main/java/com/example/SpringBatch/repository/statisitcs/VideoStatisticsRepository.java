package com.example.SpringBatch.repository.statisitcs;

import com.example.SpringBatch.entity.statisitcs.VideoStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VideoStatisticsRepository extends JpaRepository<VideoStatistics, Long> {
    List<VideoStatistics> findAllByCreatedAt(LocalDate localDate);
}
