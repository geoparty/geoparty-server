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
    private Integer rate;
    private String year;

    public Organization toEntity(String thumbnail) {
        return Organization.builder()
                .title(title)
                .summary(summary)
                .detail(detail)
                .mainAct(mainAct)
                .minDonation(minDonation)
                .thumbnail(thumbnail)
                .rate(rate)
                .year(year)
                .build();
    }

}