package com.example.SpringBatch.repository;

import com.example.SpringBatch.entity.AdViewHistory;
import com.example.SpringBatch.projection.AdStatsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.ArrayList;

public interface AdViewHistoryRepository extends JpaRepository<AdViewHistory, Long> {

    @Query(value = "SELECT video_ad_id as videoAdId, count(adview_id) as ad_view " +
            "FROM adview_history " +
            "WHERE created_at = :findDate " +
            "GROUP BY video_ad_id",
            nativeQuery = true)
    ArrayList<AdStatsProjection> findAdStatsByLocalDate(@Param("findDate") LocalDate findDate);
}
