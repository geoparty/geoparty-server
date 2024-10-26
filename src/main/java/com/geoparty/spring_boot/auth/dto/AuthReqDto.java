package com.geoparty.spring_boot.auth.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthReqDto {
    @NonNull String accessToken;
}
