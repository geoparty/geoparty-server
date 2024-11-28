package com.geoparty.spring_boot.domain.payment.dto.response;

import com.geoparty.spring_boot.domain.member.dto.MemberResponse;
import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.payment.entity.UserPointLog;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserPointLogResponse {

    private Long pointChargeid;
    private MemberResponse userData;
    private Integer chargeAmount;
    private Integer beforeCharge;
    private Integer afterCharge;
    private LocalDateTime chargeDate;

    @Builder
    public UserPointLogResponse(Long pointChargeid, MemberResponse userData, Integer chargeAmount, Integer beforeCharge, Integer afterCharge, LocalDateTime chargeDate) {
        this.pointChargeid = pointChargeid;
        this.userData = userData;
        this.chargeAmount = chargeAmount;
        this.beforeCharge = beforeCharge;
        this.afterCharge = afterCharge;
        this.chargeDate = chargeDate;
    }

    public static UserPointLogResponse from(UserPointLog userPointLog, Member member) {
        MemberResponse userData = new MemberResponse().from(member);
        return UserPointLogResponse.builder()
                .pointChargeid(userPointLog.getPointId())
                .userData(userData)
                .chargeAmount(userPointLog.getChargeAmount())
                .beforeCharge(userPointLog.getPointBefore())
                .afterCharge(userPointLog.getPointAfter())
                .chargeDate(userPointLog.getCreatedAt())
                .build();
    }
}
