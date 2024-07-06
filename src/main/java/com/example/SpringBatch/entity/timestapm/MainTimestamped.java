package com.example.SpringBatch.entity.timestapm;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class MainTimestamped {

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.DATE)
    private Date created_at;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.DATE)
    private Date updated_at;

}
