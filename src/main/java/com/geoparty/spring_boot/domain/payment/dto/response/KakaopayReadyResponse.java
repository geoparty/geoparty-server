package com.geoparty.spring_boot.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class KakaopayReadyResponse {
    @JsonProperty("tid")
    private String tid;

    // URL 변수 중 필수
    @JsonProperty("tms_result")
    private Boolean tmsResult;

    @JsonProperty("next_redirect_pc_url")
    private String nextRedirectPcUrl;

    // 아래 변수 필수는 아님
    @JsonProperty("created_at")
    private Timestamp createdAt;

    @JsonProperty("next_redirect_app_url")
    private String nextRedirectAppUrl;

    @JsonProperty("next_redirect_mobile_url")
    private String nextRedirectMobileUrl;

    @JsonProperty("android_app_scheme")
    private String androidAppScheme;

    @JsonProperty("ios_app_scheme")
    private String iosAppScheme;

}
