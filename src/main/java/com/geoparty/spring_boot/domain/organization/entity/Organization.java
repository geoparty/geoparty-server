package com.geoparty.spring_boot.domain.organization.entity;

import com.geoparty.spring_boot.global.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organization extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(length = 500)
    private String mainAct;

    @Column(nullable = false)
    private Integer minDonation;

    @Column(length = 255)
    private String thumbnail;

    @Builder
    public Organization(String title, String summary, String detail, String mainAct, Integer minDonation, String thumbnail) {
        this.title = title;
        this.summary = summary;
        this.detail = detail;
        this.mainAct = mainAct;
        this.minDonation = minDonation;
        this.thumbnail = thumbnail;
    }

}

