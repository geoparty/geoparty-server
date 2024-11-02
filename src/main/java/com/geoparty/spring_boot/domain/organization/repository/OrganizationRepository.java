package com.geoparty.spring_boot.domain.organization.repository;

import com.geoparty.spring_boot.domain.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}