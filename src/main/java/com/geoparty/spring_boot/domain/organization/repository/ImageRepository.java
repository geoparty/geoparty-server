package com.geoparty.spring_boot.domain.organization.repository;

import com.geoparty.spring_boot.domain.organization.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByOrganizationId(Long organizationId);
}
