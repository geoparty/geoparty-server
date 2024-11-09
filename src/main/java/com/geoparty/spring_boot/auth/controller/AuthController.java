package com.geoparty.spring_boot.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.geoparty.spring_boot.auth.dto.AuthReqDto;
import com.geoparty.spring_boot.auth.dto.RefreshReqDto;
import com.geoparty.spring_boot.auth.dto.SignInResponse;
import com.geoparty.spring_boot.auth.service.AuthService;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.jwt.JWTUtil;
import com.geoparty.spring_boot.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.geoparty.spring_boot.auth.vo.Token;
import com.geoparty.spring_boot.security.jwt.JWTValType;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private  final JWTUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody AuthReqDto accessToken ) throws JsonProcessingException {

        String socialAccessToken = accessToken.getAccessToken();// 카카오 엑세스 토큰

        log.info(accessToken.getAccessToken());
        //유저정보를 받아서 db에 저장

        // 카카오 엑세스 토큰으로 로그인 진행 -> 우리 서버의 jwt로 만든다.
        SignInResponse response = authService.signIn(socialAccessToken);

        return ResponseEntity.ok()
                .body(response); // body에는 accessToken,refreshToken,userInfo 전달.
    }

    @GetMapping("/userInfo")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal PrincipalDetails memberDetails) {
        // memberDetails가 null인지 확인하는 방어 코드 추가
        if (memberDetails == null || memberDetails.getMember() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");
        }

        return ResponseEntity.ok()
                .body(memberDetails.getMember());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> signOut(@AuthenticationPrincipal PrincipalDetails memberDetails) {

        // 액세스 토큰이 있다면
        if(memberDetails != null){
            int memberId = memberDetails.getMember().getMemberId();
            authService.signOut(memberId);
        }

        // 쿠키 만료 시키기
        String cookie = authService.setHttpOnlyCookieInvalidate("refreshToken");

        return ResponseEntity.ok()
                .header("Set-Cookie" , cookie)
                .build();
    }

    @DeleteMapping
    public ResponseEntity<?> withdrawal(@AuthenticationPrincipal PrincipalDetails memberDetails) {


        // 액세스 토큰이 있다면
        if(memberDetails != null){
            Integer memberId = memberDetails.getMember().getMemberId();
            authService.withdraw(memberId);//
        }

        // 쿠키 만료 시키기
        String cookie = authService.setHttpOnlyCookieInvalidate("refreshToken");

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie)
                .body(null);
    }

    // refresh token으로 refresh token 재발급 하기
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshReqDto refreshToken) {

        // 유효한 토큰이라면
        if (jwtUtil.validateToken(refreshToken.getRefreshToken()) == JWTValType.VALID_JWT) {
            Token token = authService.refresh(refreshToken.getRefreshToken());

            return ResponseEntity.ok()
                    .body(token.getRefreshToken()); // body에는 새로 발급한 access Token 반환;
        } else {
            // 유효하지 않은 토큰일 경우 401 에러 발생
            throw new BaseException(ErrorCode.UNAUTHORIZED);
        }
    }
}
