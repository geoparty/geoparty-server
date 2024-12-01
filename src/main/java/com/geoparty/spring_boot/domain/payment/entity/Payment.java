package com.geoparty.spring_boot.domain.payment.entity;

import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.entity.PartyType;
import com.geoparty.spring_boot.global.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private boolean mailed;

    @Column(nullable = false)
    private boolean completed;

    @Builder
    public Payment(Party party, Integer amount, boolean mailed, boolean completed) {
        this.party = party;
        this.amount = amount;
        this.mailed = false;
        this.completed = false;
    }

    public void updateMailed() {
        this.mailed = !this.isMailed();
    }

    public void updateCompleted() {
        this.completed = !this.isCompleted();
    }
}
