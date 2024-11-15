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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PartyResponse {

    @Schema(description = "후원하는 환경단체 이름")
    private String organization;

    @Schema(description = "환경단체 img url")
    private String imgUrl;

    @Schema(description = "파티 id")
    private Long partyId;

    @Schema(description = "파티 이름")
    private String title;

    @Schema(description = "파티 소개")
    private String intro;

    @Schema(description = "파티 시작 날짜")
    private LocalDateTime startDate;

    @Schema(description = "결제 예정 날짜")
    private LocalDate payDate;

    @Schema(description = "결제 지속 개월수")
    private Integer duration;

    @Schema(description = "목표 후원 포인트")
    private Integer targetPoint;

    @Schema(description = "파티원 수")
    private Integer size;

    @Schema(description = "1인당 후원 금액")
    private Integer pointPerPerson;

    @Schema(description = "파티 상태")
    private PartyType status;

    @Builder
    public PartyResponse(String organization, String imgUrl, Long partyId, String title, String intro, LocalDateTime startDate, LocalDate payDate, Integer duration, Integer targetPoint, Integer size, Integer pointPerPerson, PartyType status) {
        this.organization = organization;
        this.imgUrl = imgUrl;
        this.partyId = partyId;
        this.title = title;
        this.intro = intro;
        this.startDate = startDate;
        this.payDate = payDate;
        this.duration = duration;
        this.targetPoint = targetPoint;
        this.size = size;
        this.pointPerPerson = pointPerPerson;
        this.status = status;
    }

    public static PartyResponse from(Party party, Organization organization){
        return PartyResponse.builder()
                .organization(organization.getTitle())
                .imgUrl(organization.getThumbnail())
                .partyId(party.getId())
                .title(party.getTitle())
                .intro(party.getIntro())
                .payDate(party.getPayDate())
                .startDate(party.getCreatedAt())
                .duration(party.getDuration())
                .targetPoint(party.getTargetPoint())
                .size(party.getSize())
                .pointPerPerson(party.getPointPerPerson())
                .status(party.getStatus())
                .build();
    }
}
