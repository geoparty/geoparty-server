package com.geoparty.spring_boot.auth.dto;

import com.geoparty.spring_boot.auth.vo.Token;
import com.geoparty.spring_boot.domain.member.dto.MemberResponse;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SignInResponse (
        @NonNull String accessToken,
        @NonNull String refreshToken,
        @NonNull MemberResponse userData
){
    public static SignInResponse of(Token token, MemberResponse userData){
        return SignInResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .userData(userData)
                .build();
    }
}
