package com.geoparty.spring_boot.domain.payment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReadyInfoResponse {
    private String tid;
    private String webURL;

    @Builder
    public ReadyInfoResponse(String tid, String webURL) {
        this.tid = tid;
        this.webURL = webURL;
    }
}
