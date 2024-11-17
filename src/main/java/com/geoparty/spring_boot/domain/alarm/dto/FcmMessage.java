package com.geoparty.spring_boot.domain.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FcmMessage {

    @Schema(description = "유저의 FCM 토큰 정보")
    private String targetToken;

    @Schema(description = "메시지 제목")
    private String title;

    @Schema(description = "메시지 내용")
    private String content;
}