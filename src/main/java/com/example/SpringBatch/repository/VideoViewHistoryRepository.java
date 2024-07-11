package com.example.SpringBatch.repository;

import com.example.SpringBatch.entity.VideoViewHistory;
import com.example.SpringBatch.dto.VideoStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VideoViewHistoryRepository extends JpaRepository<VideoViewHistory, Long> {


    @Query(value = "SELECT new com.example.SpringBatch.dto.VideoStats" +
            "(v.video.videoId as videoId, count(v.videoViewId) as videoView, sum(v.watchTime) as videoPlaytime) " +
            "FROM VideoViewHistory v " +
            "WHERE v.createdAt = :findDate " +
            "GROUP BY v.video.videoId")
    List<VideoStats> findVideoStatsByLocalDate(@Param("findDate") LocalDate findDate);
}
