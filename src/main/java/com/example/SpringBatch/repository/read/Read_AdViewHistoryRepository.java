package com.example.springbatch.repository.read;


import com.example.springbatch.dto.AdStats;
import com.example.springbatch.entity.AdViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface Read_AdViewHistoryRepository extends JpaRepository<AdViewHistory, Long> {
    @Query(value = "SELECT new com.example.springbatch.dto.AdStats" +
            "(v.videoAd.videoAdId as videoAdId, count(v.adviewId) as adView) " +
            "FROM AdViewHistory v " +
            "WHERE v.createdAt = :findDate " +
            "GROUP BY v.videoAd.videoAdId")
    List<AdStats> findAdStatsByLocalDate(@Param("findDate") LocalDate findDate);
}
