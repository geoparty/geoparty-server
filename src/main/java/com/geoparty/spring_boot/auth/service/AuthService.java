package com.geoparty.spring_boot.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.geoparty.spring_boot.auth.dto.KakaoUserData;
import com.geoparty.spring_boot.auth.dto.SignInResponse;
import com.geoparty.spring_boot.auth.vo.Token;
import com.geoparty.spring_boot.domain.member.dto.MemberResponse;
import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.jwt.JWTUtil;
import com.geoparty.spring_boot.security.jwt.TokenProvider;
import com.geoparty.spring_boot.security.jwt.UserAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.geoparty.spring_boot.security.jwt.JWTValType.VALID_JWT;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private static final int ACCESS_TOKEN_EXPIRATION = 3 * 24 * 60 * 60 * 1000; // 3일
    private static final int REFRESH_TOKEN_EXPIRATION = 1209600000; // 2주
    private static final int ADMIN_TOKEN_EXPIRATION = 365 * 24 * 60 * 60;

    private final JWTUtil JWTUtil;
    private final MemberRepository memberRepository;
    private final KakaoService kakaoService;
//    private final TokenProvider tokenProvider;

    // 로그인(유저 정보 없다면 db에 저장하고 데이터 반환)
    @Transactional
    public SignInResponse signIn(String socialAccessToken) throws JsonProcessingException { // 카카오 액세스 토큰
        Member user = getUser(socialAccessToken); // 액세스 토큰의  유저 찾기
        Token token = getToken(user);
        return SignInResponse.of(token, MemberResponse.from(user));
    }

    // 유저 정보 없으면 저장 후 리턴
    private Member getUser(String socialAccessToken) throws JsonProcessingException {
        KakaoUserData userData = getUserData(socialAccessToken);
        return memberRepository.findBySocialId(userData.getSocialId())
                .orElseGet(() -> saveUser(userData));
    }

    // 유저 정보 카카오에서 받아오기
    private KakaoUserData getUserData(String socialAccessToken) throws JsonProcessingException {
        KakaoUserData kakaoUserData = new KakaoUserData();
        kakaoUserData.setUserData(kakaoService.getKakaoData(socialAccessToken));
        return kakaoUserData;
    }

    // 유저 정보 카카오에서 받아서 저장
    private Member saveUser(KakaoUserData userData) {

        Member user = Member.builder()
                .socialId(userData.getSocialId())
                .email(userData.getEmail())
                .nickname(userData.getNickName())
                .thumbnailImageUrl(userData.getProfileImage())
                .userIsWithdraw(false)
                .point(0) // 초기 포인트는 0으로 세팅
                .build();
        return memberRepository.save(user);
    }

    // 사용자 정보를 통해  refreshToken을 User에 저장하고 jwt Token을 반환
    private Token getToken(Member member) {
        Token token = generateToken(new UserAuthentication(member.getMemberId(), null, null)); // jwt 토큰 생성
        member.updateRefreshToken(token.getRefreshToken()); // 우리 서버의 jwt 토큰을 저장
        return token; // 토큰 반환
    }

    // 토큰 객체 생성
    private Token generateToken(Authentication authentication) {
        return Token.builder()
                .accessToken(JWTUtil.generateToken(authentication, ADMIN_TOKEN_EXPIRATION)) // 액세스 토큰 생성
                .refreshToken(JWTUtil.generateToken(authentication, REFRESH_TOKEN_EXPIRATION)) // 리프레시 토큰 생성
                .build();
    }

    // User 찾기
    private Member findUser(int id) {
        return memberRepository.findUserByMemberId(id)
                .orElseThrow();
    }

    // User 제거
    private void deleteUser(Member user) {
        user.setUserIsWithdraw(true);
    }

    // 로그아웃
    @Transactional
    public void signOut(int userId) {
        Member user = findUser(userId);
        user.resetRefreshToken();
    }

    //회원 탈퇴
    @Transactional
    public void withdraw(int userId) {
        Member user = findUser(userId);
        deleteUser(user);
    }

    // refresh Token을 http-only 쿠키로 만드는 메소드
    public String createHttpOnlyCookie(String cookieName, String cookieValue) {
        int maxAge = 60 * 60 * 24;

        ResponseCookie cookie = ResponseCookie.from(cookieName, cookieValue)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(maxAge)
                .build();

        return cookie.toString();
    }

    //리프레시 토큰 담긴 쿠키 만료 시키기
    public String setHttpOnlyCookieInvalidate(String cookieName) {

        ResponseCookie cookie = ResponseCookie.from(cookieName, null)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(0) // 쿠키 바로 만료
                .build();

        return cookie.toString();
    }

    // access token으로 access token, refresh token 재발급하는 메소드
    @Transactional
    public Token refresh(String refreshToken) {

        Integer userId = JWTUtil.getUserFromJwt(refreshToken);; //  유저 id 추출
        Member user = memberRepository.findUserByMemberId(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.UNAUTHORIZED)); //유저 정보 추출
        String realRefreshToken = user.getUserRefreshtoken(); // 저장된 refreshToken 가지고 오기

        // 저장된 리프레시 토큰이 유효하지 않다면 401 에러
        if (realRefreshToken == null ||!JWTUtil.validateToken(realRefreshToken).equals(VALID_JWT)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }

        // 저장된 리프레시 토큰의 유효성 검증
        if (!JWTUtil.validateToken(realRefreshToken).equals(VALID_JWT)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }
        // access Token 이 유효하면 엑세스 토큰, 리프레시 토큰 새로 생성 해서 반환
        return getToken(user); // Token 재생성 및 user 리프레시 토큰 컬럼에 저장한다.
        }
    }
