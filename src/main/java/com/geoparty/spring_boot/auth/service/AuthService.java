package com.geoparty.spring_boot.auth.service;

import com.geoparty.spring_boot.auth.dto.KakaoUserData;
import com.geoparty.spring_boot.auth.dto.SignInResponse;
import com.geoparty.spring_boot.auth.vo.Token;
import com.geoparty.spring_boot.domain.user.entity.UserAccount;
import com.geoparty.spring_boot.domain.user.repository.UserAccountRepository;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.filter.jwt.JwtTokenProvider;
import com.geoparty.spring_boot.security.filter.jwt.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.geoparty.spring_boot.security.filter.jwt.JwtValidationType.VALID_JWT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private static final int ACCESS_TOKEN_EXPIRATION = 1800000; // 30분
    private static final int REFRESH_TOKEN_EXPIRATION = 1209600000; // 2주

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAccountRepository userRepository;
    private final KakaoService kakaoService;


    //회원 가입 - 이미 있다면 찾은거 반환
    private UserAccount signUp(KakaoUserData userData) {
        String socialId = userData.getSocialId();
        UserAccount user = userRepository.findBySocialId(socialId)
                .orElseGet(() -> saveUser(userData));
        user.setUserIsWithdraw(false); // 재가입 한 사람 다시 살려내기
        return user;
    }

    // 유저 정보 가져오기
    private UserAccount getUser(String socialAccessToken) {
        KakaoUserData userData = getUserData(socialAccessToken);
        return signUp(userData);
    }

    private KakaoUserData getUserData(String socialAccessToken) {
        KakaoUserData kakaoUserData = new KakaoUserData();
        kakaoUserData.setUserData(kakaoService.getKakaoData(socialAccessToken));
        return kakaoUserData;
    }

    // 유저 정보 카카오에서 받아서 저장
    private UserAccount saveUser(KakaoUserData userData) {

        UserAccount user = UserAccount.builder()
                .socialId(userData.getSocialId())
                .email(userData.getEmail())
                .userIsWithdraw(false)
                .nickname(userData.getNickName())
                .build();
        return userRepository.save(user);
    }

    // 사용자 정보를 통해  refreshToken을 User에 저장하고 jwt Token을 반환
    private Token getToken(UserAccount user) {
        Token token = generateToken(new UserAuthentication(user.getUserId(), null, null)); // jwt 토큰 생성
        user.updateRefreshToken(token.getRefreshToken()); // 우리 서버의 jwt 토큰을 저장
        return token; // 토큰 반환
    }

    // 토큰 객체 생성
    private Token generateToken(Authentication authentication) {
        return Token.builder()
                .accessToken(jwtTokenProvider.generateToken(authentication, ACCESS_TOKEN_EXPIRATION)) // 액세스 토큰 생성
                .refreshToken(jwtTokenProvider.generateToken(authentication, REFRESH_TOKEN_EXPIRATION)) // 리프레시 토큰 생성
                .build();
    }

    // User 찾기
    private UserAccount findUser(int id) {
        return userRepository.findUserByUserId(id)
                .orElseThrow();
    }

    // User 제거
    private void deleteUser(UserAccount user) {
        user.setUserIsWithdraw(true);
    }


    // 로그인
    @Transactional
    public SignInResponse signIn(String socialAccessToken) { // 액세스 토큰
        UserAccount user = getUser(socialAccessToken); // 액세스 토큰의  유저 찾기
        Token token = getToken(user);
        return SignInResponse.of(token);
    }

    // 로그아웃
    @Transactional
    public void signOut(int userId) {
        UserAccount user = findUser(userId);
        user.resetRefreshToken();
    }

    //회원 탈퇴
    @Transactional
    public void withdraw(int userId) {
        UserAccount user = findUser(userId);
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

    // refresh token으로 access token, refresh token 재발급하는 메소드
    @Transactional
    public Token refresh(String refreshToken) {

        if (!Objects.equals(jwtTokenProvider.validateToken(refreshToken), VALID_JWT)) { // 유효하지 않은 토큰이면 401 에러
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }
        Integer userId = jwtTokenProvider.getUserFromJwt(refreshToken); //  정보 추출
        UserAccount user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.UNAUTHORIZED)); //유저 정보 추출
        String realRefreshToken = user.getUserRefreshtoken(); // 저장된 refreshToken 가지고 오기

        // 저장된 토큰이 유효하지 않다면 오류 반환
        if (!jwtTokenProvider.validateToken(realRefreshToken).equals(VALID_JWT)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }

        // refresh token이 같다면 토큰 생성 해서 반환
        if (realRefreshToken.equals(refreshToken)) {
            return getToken(user); // Token 재생성 및 user 리프레시 토큰 컬럼에 저장한다.
        }

        // 리프레시 토큰이 같지 않다면 토큰 오류 반환
        throw new BaseException(ErrorCode.UNAUTHORIZED);
    }

}