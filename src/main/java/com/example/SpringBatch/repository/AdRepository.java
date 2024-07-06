package com.example.SpringBatch.repository;

import com.example.SpringBatch.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdRepository extends JpaRepository<Ad, UUID> {
}
