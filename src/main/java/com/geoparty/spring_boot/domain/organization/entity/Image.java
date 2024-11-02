package com.geoparty.spring_boot.domain.organization.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public Image(String imageUrl, Organization organization) {
        this.imageUrl = imageUrl;
        this.organization = organization;
    }
}