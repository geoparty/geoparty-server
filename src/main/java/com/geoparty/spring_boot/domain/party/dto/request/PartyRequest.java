package com.geoparty.spring_boot.domain.party.dto.request;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.entity.PartyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class PartyRequest {

    private String title;
    private String intro;
    private Integer targetPoint;
    private Integer pointPerPerson;
    private Organization organization;

    public Party toEntity(Member member, Integer count) {
        return Party.builder()
                .host(member)
                .title(title)
                .intro(intro)
                .targetPoint(targetPoint)
                .pointPerPerson(pointPerPerson)
                .count(count)
                .organization(organization)
                .build();
    }
}
