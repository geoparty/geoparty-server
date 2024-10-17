package com.geoparty.spring_boot.domain.party.entity;

import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.user.entity.UserAccount;
import com.geoparty.spring_boot.global.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Party extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String intro;

    private LocalDateTime pay_date;

    @Column(nullable = false)
    private Integer total_point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private UserAccount host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Builder
    public Party(String title, String intro, Integer total_point, UserAccount host, Organization organization) {
        this.title = title;
        this.intro = intro;
        this.total_point = 0;
        this.host = host;
        this.organization = organization;
    }
}
