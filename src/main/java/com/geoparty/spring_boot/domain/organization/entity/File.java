package com.geoparty.spring_boot.domain.organization.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public File(String fileUrl, Organization organization) {
        this.fileUrl = fileUrl;
        this.organization = organization;
    }
}