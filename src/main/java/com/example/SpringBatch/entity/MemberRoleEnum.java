package com.example.springbatch.entity;

import lombok.Getter;

@Getter
public enum MemberRoleEnum {
    BUYER(Authority.BUYER),    // 구매자 - 일반 사용자
    SELLER(Authority.SELLER),  // 판매자
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authority;

    MemberRoleEnum(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String BUYER = "BUYER";
        public static final String SELLER = "SELLER";
        public static final String ADMIN = "ADMIN";
    }
}