package com.example.SpringBatch.repository.statisitcs;

import com.example.SpringBatch.entity.statisitcs.AdStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AdStatisticsRepository extends JpaRepository<AdStatistics, Long> {
    List<AdStatistics> findAllByCreatedAt(LocalDate now);
}
