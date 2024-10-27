package com.geoparty.spring_boot.domain.organization.dto.request;

import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.party.entity.Party;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@Getter
public class OrgRequest {

    private String title;
    private String summary;
    private String detail;
    private String mainAct;
    private Integer minDonation;
    private MultipartFile thumbnail; // 대표 이미지
    private List<MultipartFile> detailedPhotos; // 여러 개의 상세 이미지

    public Organization toEntity(String thumbnail) {
        return Organization.builder()
                .title(title)
                .summary(summary)
                .detail(detail)
                .mainAct(mainAct)
                .thumbnail(thumbnail)
                .build();
    }

}