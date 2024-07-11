package com.example.SpringBatch.entity;

import com.example.SpringBatch.entity.timestapm.MainTimestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends MainTimestamped {

    @Id
    private String memberId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRoleEnum authority;

    @Column(length = 20)
    private String social;

    @Column(nullable = false)
    private Boolean deleteCheck;
}
