package com.geoparty.spring_boot.domain.organization.service;

import com.geoparty.spring_boot.domain.organization.dto.request.OrgRequest;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface OrgService {
    void createOrganization(OrgRequest request); // 환경 단체 등록
    Page<Organization> getOrganizations(Pageable pageable); // 환경 단체 리스트 조회

}
