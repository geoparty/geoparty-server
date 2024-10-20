package com.geoparty.spring_boot.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class AuthReqDto {
    @NonNull String accessToken;
}
