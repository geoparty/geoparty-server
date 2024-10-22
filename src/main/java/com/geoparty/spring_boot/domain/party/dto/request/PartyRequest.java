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
    private String imgUrl;
    private Organization organization;
    private Integer targetPoint;
    private Integer pointPerPerson;

    public Party toEntity(String imgUrl, Member member) {
        return Party.builder()
                .title(title)
                .intro(intro)
                .imgUrl(imgUrl)
                .host(member)
                .organization(organization)
                .targetPoint(targetPoint)
                .pointPerPerson(pointPerPerson)
                .build();
    }
}
