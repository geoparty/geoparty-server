package com.geoparty.spring_boot.domain.payment.dto.response;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.payment.entity.Payment;
import com.geoparty.spring_boot.domain.payment.entity.UserPayment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserPaymentResponse {

    private Long id;
    private Integer memberId;
    private String partyName;
    private Integer amount;
    private LocalDateTime date;

    @Builder
    public UserPaymentResponse(Long id, Integer memberId, String partyName, Integer amount, LocalDateTime date) {
        this.id = id;
        this.memberId = memberId;
        this.partyName = partyName;
        this.amount = amount;
        this.date = date;
    }

    public static UserPaymentResponse from(UserPayment userPayment){
        return UserPaymentResponse.builder()
                .id(userPayment.getId())
                .memberId(userPayment.getMember().getMemberId())
                .partyName(userPayment.getParty().getTitle())
                .amount(userPayment.getAmount())
                .date(userPayment.getCreatedAt())
                .build();
    }
}
