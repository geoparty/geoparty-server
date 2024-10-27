package com.geoparty.spring_boot.auth.controller;

import com.geoparty.spring_boot.auth.dto.AuthReqDto;
import com.geoparty.spring_boot.auth.dto.RefreshReqDto;
import com.geoparty.spring_boot.auth.dto.SignInResponse;
import com.geoparty.spring_boot.auth.service.AuthService;
import com.geoparty.spring_boot.domain.member.dto.MemberDto;
import com.geoparty.spring_boot.domain.member.service.MemberServiceImpl;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.geoparty.spring_boot.auth.vo.Token;
import com.geoparty.spring_boot.security.jwt.JWTValType;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private  final JWTUtil jwtUtil;

    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody AuthReqDto accessToken ) {

        String socialAccessToken = "Bearer " + accessToken.getAccessToken();// 카카오 엑세스 토큰

        log.info(accessToken.getAccessToken());
        //유저정보를 받아서 db에 저장

        // 카카오 엑세스 토큰으로 로그인 진행 -> 우리 서버의 jwt로 만든다.
        SignInResponse response = authService.signIn(socialAccessToken);

        return ResponseEntity.ok()
                .body(response); // body에는 accessToken,refreshToken,userInfo 전달.
    }

    @GetMapping("/userInfo")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal MemberDto member) {
        // "Bearer " 부분 제거하고 순수한 토큰 값만 추출
//        String accessToken = authorizationHeader.replace("Bearer ", "");
//
//        MemberDto userData = memberService.getUserInfo(accessToken);
        return ResponseEntity.ok()
                .body(member);
    }

    //    @GetMapping
    //    public ResponseEntity<?> getOrganization(HttpServletRequest request) {
    //        Integer userId = jwtUtil.getMemberId(request)
    //                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_TOKEN));// 헤더의 access token으로 userId 추출, null 반환시 유효하지 않은 토큰 오류 전송
    //        return ResponseEntity.status(HttpStatus.OK);
    //    }

    @PostMapping("/logout")
    public ResponseEntity<?> signOut(@AuthenticationPrincipal MemberDto member) {

        // "Bearer " 부분 제거하고 토큰 값만 추출
        // String accessToken = authorizationHeader.replace("Bearer ", "");

        // 액세스 토큰이 있다면
        if(member != null){
            Integer memberId = member.getMemberId();
            authService.signOut(memberId);
        }

        // 쿠키 만료 시키기
        String cookie = authService.setHttpOnlyCookieInvalidate("refreshToken");

        return ResponseEntity.ok()
                .header("Set-Cookie" , cookie)
                .build();
    }

    @DeleteMapping
    public ResponseEntity<?> withdrawal(@AuthenticationPrincipal MemberDto member) {


        // 액세스 토큰이 있다면
        if(member != null){
            Integer memberId = member.getMemberId();
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
                    .body(token); // body에는 새로 발급한 access,refresh token 넣는다.;
        } else {
            // 유효하지 않은 토큰일 경우 401 에러 발생
            throw new BaseException(ErrorCode.UNAUTHORIZED);
        }
    }
}
