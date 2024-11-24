package com.geoparty.spring_boot.domain.payment.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class KakaopayCardInfoResponse {
    private String kakaopayPurchaseCorp;
    private String kakaopayPurchaseCorpCode;
    private String kakaopayIssuerCorp;
    private String kakaopayissuerCorpCode;
    private String bin;
    private String cardType;
    private String installMonth;
    private String approvedId;
    private String cardMid;
    private String interestFreeInstall;
    private String installmentType;
    private String cardItemCode;
}
