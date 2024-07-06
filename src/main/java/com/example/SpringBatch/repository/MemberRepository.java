package com.example.SpringBatch.repository;

import com.example.SpringBatch.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,String> {
}
