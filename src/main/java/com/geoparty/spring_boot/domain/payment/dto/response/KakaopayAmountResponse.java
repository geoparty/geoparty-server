package com.geoparty.spring_boot.domain.payment.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class KakaopayAmountResponse  {
    private int total;
    private int taxFree;
    private int vat;
    private int point;
    private int discount;
    private int greenDeposit;
}