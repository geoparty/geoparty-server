package com.geoparty.spring_boot.domain.party.entity;

import lombok.Getter;

@Getter
public enum PartyType {
    PENDING("대기중"),
    PROGRESS("정기 결제 시작"),
    HALTED("정기 결제 중단");

    private final String value;

    PartyType(String value) {
        this.value = value;
    }
}
