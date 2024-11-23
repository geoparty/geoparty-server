package com.geoparty.spring_boot.domain.payment.dto.response;

import com.geoparty.spring_boot.domain.payment.entity.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long partyId;
    private Integer amount;
    private LocalDateTime date;
    private boolean mailed;

    @Builder
    public PaymentResponse(Long id, Long partyId, Integer amount, LocalDateTime date, boolean mailed) {
        this.id = id;
        this.partyId = partyId;
        this.amount = amount;
        this.date = date;
        this.mailed = mailed;
    }

    public static PaymentResponse from(Payment payment){
        return PaymentResponse.builder()
                .id(payment.getId())
                .partyId(payment.getParty().getId())
                .amount(payment.getAmount())
                .date(payment.getCreatedAt())
                .build();
    }
}
