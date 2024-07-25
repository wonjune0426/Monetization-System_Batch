package com.example.springbatch.repository.read;

import com.example.springbatch.entity.statisitcs.AdStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface Read_AdStatisticsRepository extends JpaRepository<AdStatistics, Long> {
    List<AdStatistics> findAllByCreatedAt(LocalDate localDate);
}
