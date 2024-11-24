package com.geoparty.spring_boot.domain.payment.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.security.Timestamp;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class KakaopayApproveResponse {
    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private KakaopayAmountResponse amount;
    private KakaopayCardInfoResponse cardInfo;
    private String itemName;
    private String itemCode;
    private String quantity;
    private Timestamp createdAt;
    private Timestamp approvedAt;
    private String payload;
}