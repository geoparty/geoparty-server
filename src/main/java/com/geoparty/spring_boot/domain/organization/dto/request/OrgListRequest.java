package com.geoparty.spring_boot.domain.organization.dto.request;

import lombok.*;
import org.springframework.data.domain.Sort;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgListRequest {
    @Builder.Default
    private String order = "orgId";

    @Builder.Default
    private Sort.Direction direction = Sort.Direction.DESC; // 정렬순서(내림차순)

    @Builder.Default
    private Integer listNum = 7; // 한 페이지당 7개 목록

    @Builder.Default
    private Integer pageNum = 1; // 1 페이지씩 조회

}
