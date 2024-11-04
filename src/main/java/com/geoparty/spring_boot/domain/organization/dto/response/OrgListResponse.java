package com.geoparty.spring_boot.domain.organization.dto.response;

import com.geoparty.spring_boot.domain.organization.entity.Organization;
import lombok.*;

import java.util.List;

@Data
@Builder
public class OrgListResponse {
    private List<OrgDTO> orgs;
//    private Integer pageNum;
//    private Integer length;
//    private Integer totalPage; // 전체 페이지

    @Getter
    @Setter
    @Builder
    public static class OrgDTO {
        private Long orgId;
        private String orgTitle; // 단체 이름
        private String thumbnail; // 단체 썸네일
        private String orgSummary; // 단체 간단 설명
        private Integer partyNum; // 몇개의 파티가 후원중인지
    }

    public static class OrgListResponseBuilder {
        public OrgListResponseBuilder orgs(List<Organization> orgs) {
            this.orgs = orgs.stream()
                    .map(o -> OrgDTO.builder()
                            .orgId(o.getId())
                            .orgTitle(o.getTitle())
                            .thumbnail(o.getThumbnail())
                            .orgSummary(o.getSummary())
                            .partyNum(3) // to-do : 파티랑 연결해서 변경
                            .build())
                    .toList();
            return this;
        }
    }
}