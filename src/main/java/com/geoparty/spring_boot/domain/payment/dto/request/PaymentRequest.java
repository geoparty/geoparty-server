package com.geoparty.spring_boot.domain.payment.dto.request;

import com.geoparty.spring_boot.domain.payment.entity.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentRequest {


    private Integer amount;

    @Builder
    public PaymentRequest(Integer amount) {
        this.amount = amount;
    }

}
