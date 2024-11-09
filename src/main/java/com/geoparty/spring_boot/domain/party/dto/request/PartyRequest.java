package com.geoparty.spring_boot.domain.party.dto.request;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.party.entity.Party;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PartyRequest {

    private String title;
    private String intro;
    private Integer targetPoint;
    private Integer pointPerPerson;
    private Integer size;
    private Organization organization;

    public Party toEntity(Member member) {
        return Party.builder()
                .host(member)
                .title(title)
                .intro(intro)
                .targetPoint(targetPoint)
                .pointPerPerson(pointPerPerson)
                .size(size)
                .organization(organization)
                .build();
    }
}
