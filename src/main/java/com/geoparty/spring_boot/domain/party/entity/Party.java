package com.geoparty.spring_boot.domain.party.entity;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.global.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.geoparty.spring_boot.domain.party.entity.PartyType.A;
import static com.geoparty.spring_boot.domain.party.entity.PartyType.C;

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

//    private String imgUrl;

    private LocalDate payDate;

//    @Column(nullable = false)
//    private Integer totalPoint; // 실제 모인 포인트

    @Column(nullable = false)
    private Integer targetPoint; // 후원 목표 포인트

    @Column(nullable = false)
    private Integer pointPerPerson; // 1인당 후원 포인트

    @Column(nullable = false)
    private Integer size; // 파티 멤버 정원

    private Integer duration; // 결제 지속 개월수

    @Enumerated(EnumType.STRING)
    private PartyType status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Member host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Builder
    public Party(String title, String intro, Integer targetPoint, Integer pointPerPerson, Integer size, Integer duration, PartyType status, Member host, Organization organization) {
        this.title = title;
        this.intro = intro;
        this.targetPoint = targetPoint;
        this.pointPerPerson = pointPerPerson;
        this.size = size;
        this.duration = 0;
        this.status = A;
        this.host = host;
        this.organization = organization;
    }

    public void updatePartyType(final PartyType status) {
        this.status = status;
    }

    public void updateDuration() {
        this.duration += 1;
    }

    public void resetPayDate() {
        this.payDate = LocalDate.now().minusMonths(1);
    }


    public void updatePayDate() {
        this.payDate = LocalDate.now().plusMonths(1);
    }

    public void extendPayDate() {
        this.payDate = LocalDate.now().plusDays(3);
    }

    public void updatePayDateToToday() {
        this.payDate = LocalDate.now();
    }
}
