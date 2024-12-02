package com.geoparty.spring_boot.domain.organization.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrgDTO {
    private Long orgId;
    private String orgTitle; // 단체 이름
    private String thumbnail; // 단체 썸네일
    private String orgSummary; // 단체 간단 설명
    private Integer partyNum; // 몇개의 파티가 후원중인지
}