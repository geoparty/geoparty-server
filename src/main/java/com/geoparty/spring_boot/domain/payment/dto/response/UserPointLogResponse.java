package com.geoparty.spring_boot.domain.payment.dto.response;

import com.geoparty.spring_boot.domain.member.dto.MemberResponse;
import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.payment.entity.Point;
import com.geoparty.spring_boot.domain.payment.entity.UserPayment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserPointLogResponse {

    private Long id;
    private MemberResponse userData;
    private Integer chargeAmount;
    private Integer beforeCharge;
    private Integer afterCharge;
    private LocalDateTime chargeDate;

    @Builder
    public UserPointLogResponse(Long id, MemberResponse userData, Integer chargeAmount, Integer beforeCharge, Integer afterCharge, LocalDateTime chargeDate) {
        this.id = id;
        this.userData = userData;
        this.chargeAmount = chargeAmount;
        this.beforeCharge = beforeCharge;
        this.afterCharge = afterCharge;
        this.chargeDate = chargeDate;
    }

//    public static UserPointLogResponse from(Point point){
//        return UserPointLogResponse.builder()
//                .id(userPayment.getId())
//                .memberId(userPayment.getMember().getMemberId())
//                .partyName(userPayment.getParty().getTitle())
//                .amount(userPayment.getAmount())
//                .date(userPayment.getCreatedAt())
//                .build();
//    }
}
