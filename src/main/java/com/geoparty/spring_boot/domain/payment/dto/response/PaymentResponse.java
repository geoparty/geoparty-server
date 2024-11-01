package com.geoparty.spring_boot.domain.payment.dto.response;

import com.geoparty.spring_boot.domain.payment.entity.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentResponse {

    private Integer amount;
    private LocalDateTime date;

    @Builder
    public PaymentResponse(Integer amount, LocalDateTime date) {
        this.amount = amount;
        this.date = date;
    }

    public static PaymentResponse from(Payment payment){
        return PaymentResponse.builder()
                .amount(payment.getAmount())
                .date(payment.getCreatedAt())
                .build();
    }
}
