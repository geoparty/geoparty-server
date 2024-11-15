package com.geoparty.spring_boot.domain.payment.entity;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.global.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPayment extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Column(nullable = false)
    private Integer amount;

    @Builder
    public UserPayment(Member member, Party party, Integer amount) {
        this.member = member;
        this.party = party;
        this.amount = amount;
    }
}