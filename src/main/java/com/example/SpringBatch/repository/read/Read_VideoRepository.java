package com.example.springbatch.repository.read;

import com.example.springbatch.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface Read_VideoRepository extends JpaRepository<Video, UUID> {
}
