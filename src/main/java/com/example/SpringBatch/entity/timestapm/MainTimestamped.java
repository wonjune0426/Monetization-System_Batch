package com.example.springbatch.entity.timestapm;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class MainTimestamped {

    @CreatedDate
    @Column(updatable = false)
    private LocalDate created_at;

    @LastModifiedDate
    @Column
    private LocalDate updated_at;

}
