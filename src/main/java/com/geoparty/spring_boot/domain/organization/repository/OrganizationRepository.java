package com.geoparty.spring_boot.domain.organization.repository;

import com.geoparty.spring_boot.domain.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
