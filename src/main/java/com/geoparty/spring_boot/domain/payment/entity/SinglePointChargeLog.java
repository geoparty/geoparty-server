package com.geoparty.spring_boot.domain.payment.entity;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.global.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SinglePointChargeLog extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_log_id")
    private Long id;

    @Column(name = "point")
    private int point;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;


    @Column(name = "point_log_status")
    @Setter private String pointLogStatus;

    @Column(name="tid")
    @Setter private String tid;

    @Builder
    public SinglePointChargeLog(int point, Member member, String pointLogStatus, String tid) {
        this.point = point;
        this.member = member;
        this.pointLogStatus = pointLogStatus;
    }
}
