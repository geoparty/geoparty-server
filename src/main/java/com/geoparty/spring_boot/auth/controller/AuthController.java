package com.geoparty.spring_boot.auth.controller;

import com.geoparty.spring_boot.auth.dto.AuthReqDto;
import com.geoparty.spring_boot.auth.dto.SignInResponse;
import com.geoparty.spring_boot.auth.service.AuthService;
import com.geoparty.spring_boot.auth.service.KakaoService;
import com.geoparty.spring_boot.domain.member.dto.MemberDto;
import com.geoparty.spring_boot.domain.member.service.MemberServiceImpl;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.geoparty.spring_boot.auth.vo.Token;
import com.geoparty.spring_boot.security.jwt.JWTValType;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private  final MemberServiceImpl memberService;
    private  final JWTUtil jwtUtil;
    private final KakaoService kakaoService;
    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody AuthReqDto accessToken ) {

        String socialAccessToken = "Bearer " + accessToken.getAccessToken();// 카카오 엑세스 토큰

        log.info(accessToken.getAccessToken());
        //유저정보를 받아서 db에 저장


        // 카카오 엑세스 토큰으로 로그인 진행 -> 우리 서버의 jwt로 만든다.
        SignInResponse response = authService.signIn(socialAccessToken);

        return ResponseEntity.ok()
                .body(response.accessToken()); // body에는 accessToken,refreshToken,userInfo 전달.
    }

    @GetMapping("/userInfo")
    public ResponseEntity<?> getUserInfo(@RequestBody AuthReqDto accessToken ) {
        MemberDto userData = memberService.getUserInfo(accessToken.getAccessToken());
        return ResponseEntity.ok()
                .body(userData);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> signOut(@RequestBody AuthReqDto accessToken) {
        // 액세스 토큰이 있다면
        if(accessToken != null){
            int userId = jwtUtil.getUserFromJwt(accessToken.getAccessToken());
            authService.signOut(userId); // DB내 저장된 리프레시 토큰 삭제
        }

        // 쿠키 만료 시키기
        String cookie = authService.setHttpOnlyCookieInvalidate("refreshToken");

        return ResponseEntity.ok()
                .header("Set-Cookie" , cookie)
                .build();
    }

    @DeleteMapping
    public ResponseEntity<?> withdrawal(@RequestBody AuthReqDto accessToken) {

        // 액세스 토큰이 있다면
        if(accessToken != null){
            int userId = jwtUtil.getUserFromJwt(accessToken.getAccessToken());
            authService.withdraw(userId);// DB내 저장된 리프레시 토큰 삭제
        }

        // 쿠키 만료 시키기
        String cookie = authService.setHttpOnlyCookieInvalidate("refreshToken");

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie)
                .body(null);
    }

    // refresh token으로 refresh token 재발급 하기
    @GetMapping("/refresh")
    public ResponseEntity<?> isLogin(@RequestBody AuthReqDto accessToken) {
        String memberToken = accessToken.getAccessToken();
        // 유효한 엑세스 토큰이라면
        if (jwtUtil.validateToken(memberToken) == JWTValType.VALID_JWT) {
            Token token = authService.refresh(memberToken);

            return ResponseEntity.ok()
                    .body(token); // body에는 새로 발급한 access token, refresh token 넣는다.;
        } else {
            // 유효하지 않은 토큰일 경우 401 에러 발생
            throw new BaseException(ErrorCode.UNAUTHORIZED);
        }
    }
}
