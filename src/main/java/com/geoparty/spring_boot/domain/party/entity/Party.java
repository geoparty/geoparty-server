package com.geoparty.spring_boot.domain.party.entity;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.global.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.geoparty.spring_boot.domain.party.entity.PartyType.PENDING;

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

    private String imgUrl;

    private LocalDateTime payDate;

    @Column(nullable = false)
    private Integer totalPoint; // 실제 모인 금액

    @Column(nullable = false)
    private Integer targetPoint; // 목표 금액

    @Column(nullable = false)
    private Integer pointPerPerson; // 1인당 후원 금액

    private Integer duration; // 결제 지속 금액

    @Enumerated(EnumType.STRING)
    private PartyType status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Member host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Builder
    public Party(String title, String intro, String imgUrl, Integer totalPoint, Integer targetPoint, Integer pointPerPerson, Integer duration, PartyType status, Member host, Organization organization) {
        this.title = title;
        this.intro = intro;
        this.imgUrl = imgUrl;
        this.totalPoint = 0;
        this.targetPoint = targetPoint;
        this.pointPerPerson = pointPerPerson;
        this.duration = 0;
        this.status = PENDING;
        this.host = host;
        this.organization = organization;
    }
}
