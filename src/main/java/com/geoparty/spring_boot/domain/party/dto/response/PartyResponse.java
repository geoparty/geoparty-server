package com.geoparty.spring_boot.domain.party.dto.response;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.entity.PartyType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PartyResponse {
    private String title;
    private String imgUrl;
    private LocalDateTime payDate;
    private Integer totalPoint;

    @Builder
    public PartyResponse(String title, String imgUrl, LocalDateTime payDate, Integer totalPoint) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.payDate = payDate;
        this.totalPoint = totalPoint;
    }

    public static PartyResponse from(Party party){
        return PartyResponse.builder()
                .title(party.getTitle())
                .imgUrl(party.getImgUrl())
                .payDate(party.getPayDate())
                .totalPoint(party.getTotalPoint())
                .build();
    }
}
