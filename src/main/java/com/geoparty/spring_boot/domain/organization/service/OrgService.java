package com.geoparty.spring_boot.domain.organization.service;

import com.geoparty.spring_boot.domain.organization.dto.request.OrgListRequest;
import com.geoparty.spring_boot.domain.organization.dto.request.OrgRequest;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgListResponse;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgResponse;


public interface OrgService {
    void createOrganization(OrgRequest request); // 환경 단체 등록
    OrgListResponse getOrganizations(OrgListRequest request); // 환경 단체 리스트 조회

    OrgResponse getDetail(Long orgId); // 환경 단체 상세 조회

}
