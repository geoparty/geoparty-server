package com.geoparty.spring_boot.domain.party.dto.response;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.entity.PartyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PartyResponse {

    @Schema(description = "후원하는 환경단체 이름")
    private String organization;

    @Schema(description = "환경단체 img url")
    private String imgUrl;

    @Schema(description = "파티 이름")
    private String title;

    @Schema(description = "파티 소개")
    private String intro;

    @Schema(description = "결제 예정 날짜")
    private LocalDateTime payDate;

    @Schema(description = "결제 지속 개월수")
    private Integer duration;

    @Schema(description = "목표 후원 포인트")
    private Integer targetPoint;

    @Schema(description = "실제 모인 포인트")
    private Integer totalPoint;

    @Schema(description = "1인당 후원 금액")
    private Integer pointPerPerson;

    @Schema(description = "파티 상태")
    private PartyType status;

    @Builder
    public PartyResponse(String organization, String imgUrl, String title, String intro, LocalDateTime payDate, Integer duration, Integer targetPoint, Integer totalPoint, Integer pointPerPerson, PartyType status) {
        this.organization = organization;
        this.imgUrl = imgUrl;
        this.title = title;
        this.intro = intro;
        this.payDate = payDate;
        this.duration = duration;
        this.targetPoint = targetPoint;
        this.totalPoint = totalPoint;
        this.pointPerPerson = pointPerPerson;
        this.status = status;
    }

    public static PartyResponse from(Party party, Organization organization){
        return PartyResponse.builder()
                .organization(organization.getTitle())
                .imgUrl(organization.getThumbnail())
                .title(party.getTitle())
                .intro(party.getIntro())
                .payDate(party.getPayDate())
                .duration(party.getDuration())
                .targetPoint(party.getTargetPoint())
                .totalPoint(party.getTotalPoint())
                .pointPerPerson(party.getPointPerPerson())
                .status(party.getStatus())
                .build();
    }
}
