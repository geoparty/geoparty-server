package com.geoparty.spring_boot.domain.organization.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrgListResponse {
    private List<OrgDTO> orgs;
}