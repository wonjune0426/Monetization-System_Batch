package com.example.SpringBatch.repository;

import com.example.SpringBatch.entity.VideoViewHistory;
import com.example.SpringBatch.projection.VideoStatsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.ArrayList;

public interface VideoViewHistoryRepository extends JpaRepository<VideoViewHistory, Long> {


    @Query(value = "SELECT BIN_TO_UUID(video_id) as videoId, count(videoview_id) as videoView, sum(watch_time) as videoPlaytime " +
            "FROM videoview_history " +
            "WHERE created_at = :findDate " +
            "GROUP BY video_id",
            nativeQuery = true)
    ArrayList<VideoStatsProjection> findVideoStatsByLocalDate(@Param("findDate") LocalDate findDate);
}
