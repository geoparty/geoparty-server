package com.geoparty.spring_boot.domain.payment.entity;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.global.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends AuditingFields {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "point_id", nullable = false)
    private Long pointId;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "before_charge", nullable = false)
    private int pointBefore;

    @Column(name = "after_charge", nullable = false)
    private int pointAfter;

    @Column(name = "charge_amount", nullable = false)
    private int chargeAmount;

    @Column(name = "charge_content", nullable = false, length = 100)
    private String chargeContent;

    @Builder
    public Point(Member member, int pointBefore, int pointAfter, int chargeAmount, String chargeContent) {
        this.member = member;
        this.pointBefore = pointBefore;
        this.pointAfter = pointAfter;
        this.chargeAmount = chargeAmount;
        this.chargeContent = chargeContent;
    }
}