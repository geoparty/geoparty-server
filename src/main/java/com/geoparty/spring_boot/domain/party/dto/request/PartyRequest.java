package com.geoparty.spring_boot.domain.party.dto.request;

import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.user.entity.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class PartyRequest {

    private String title;
    private String intro;
    private UserAccount host;
    private Organization organization;

    public Party toEntity() {
        return Party.builder()
                .title(title)
                .intro(intro)
                .host(host)
                .organization(organization)
                .build();
    }
}
