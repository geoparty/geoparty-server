package com.geoparty.spring_boot.domain.payment.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KakaopayApproveRequest {
    String cid;
    String tid;
    String partner_order_id;
    String partner_user_id;
    String pg_token;
}
