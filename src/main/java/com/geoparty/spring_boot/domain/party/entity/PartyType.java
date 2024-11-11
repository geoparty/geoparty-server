package com.geoparty.spring_boot.domain.party.entity;

import lombok.Getter;

@Getter
public enum PartyType {
    A("초기화, 결제 대기중"),
    B("정상적으로 정기 결제 중"),
    C("초기 결제 대기중 or 멤버 부족으로 정기 결제 중단, 대기중"),
    D("포인트 미달로 정기 결제 중단");

    private final String value;

    PartyType(String value) {
        this.value = value;
    }
}
