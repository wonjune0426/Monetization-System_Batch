package com.example.SpringBatch.repository;

import com.example.SpringBatch.dto.AdStats;
import com.example.SpringBatch.entity.AdViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AdViewHistoryRepository extends JpaRepository<AdViewHistory, Long> {

    @Query(value = "SELECT new com.example.SpringBatch.dto.AdStats" +
            "(v.videoAd.videoAdId as videoAdId, count(v.adviewId) as adView) " +
            "FROM AdViewHistory v " +
            "WHERE v.createdAt = :findDate " +
            "GROUP BY v.videoAd.videoAdId")
    List<AdStats> findAdStatsByLocalDate(@Param("findDate") LocalDate findDate);
}
