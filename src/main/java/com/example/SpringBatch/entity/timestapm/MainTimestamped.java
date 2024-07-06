package com.example.SpringBatch.entity.timestapm;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;

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
