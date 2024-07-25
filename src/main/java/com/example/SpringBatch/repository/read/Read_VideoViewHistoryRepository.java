package com.example.springbatch.repository.read;

import com.example.springbatch.dto.VideoStats;
import com.example.springbatch.entity.VideoViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface Read_VideoViewHistoryRepository extends JpaRepository<VideoViewHistory, Long> {
    @Query(value = "SELECT new com.example.springbatch.dto.VideoStats" +
            "(v.video.videoId as videoId, count(v.videoViewId) as videoView, sum(v.watchTime) as videoPlaytime) " +
            "FROM VideoViewHistory v " +
            "WHERE v.createdAt = :findDate " +
            "GROUP BY v.video.videoId")
    List<VideoStats> findVideoStatsByLocalDate(@Param("findDate") LocalDate findDate);
}
