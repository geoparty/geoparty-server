package com.geoparty.spring_boot.domain.organization.service;

import com.geoparty.spring_boot.domain.organization.dto.request.OrgListRequest;
import com.geoparty.spring_boot.domain.organization.dto.request.OrgRequest;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgListResponse;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface OrgService {
    void createOrganization(OrgRequest request, MultipartFile thumbnail, List<MultipartFile> photos, MultipartFile pdf); // 환경 단체 등록

    OrgResponse getDetail(Long orgId); // 환경 단체 상세 조회

    OrgListResponse getOrganizations();

}
