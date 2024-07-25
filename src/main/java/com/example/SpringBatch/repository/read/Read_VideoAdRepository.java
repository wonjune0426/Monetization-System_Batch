package com.example.springbatch.repository.read;

import com.example.springbatch.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface Read_VideoAdRepository extends JpaRepository<VideoAd, Long> {
}
