package com.geoparty.spring_boot.domain.organization.repository;

import com.geoparty.spring_boot.domain.organization.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    File findByOrganizationId(Long organizationId);
}
