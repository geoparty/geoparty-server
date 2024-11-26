package com.geoparty.spring_boot.security.jwt;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.security.model.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.*;

import java.io.IOException;


// JWT 인증 필터로 요청에 JWT를 검증하는 클래스
@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
//            val token = tokenProvider.getAccessTokenFromRequest(request);
//            log.debug("Extracted Token: {}", token);
//            if (StringUtils.hasText(token) && jwtUtil.validateToken(token) == JWTValType.VALID_JWT) { // null 이 아니고 유효하다면
//                log.debug("Token is valid");
//                val authentication = new UserAuthentication(getUserId(token), null, null); // 사용자의 식별자를 추출하지
//                log.debug("User Authentication: {}", authentication);
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } else {
//                log.warn("Token is invalid or empty");
//            }
            // 요청 URI 가져오기
            String requestUri = request.getRequestURI();

            // permitAll 경로는 스킵
            if (request.getRequestURI().startsWith("/api/") &&
                    (request.getMethod().equals("GET") || request.getMethod().equals("POST"))) {
                filterChain.doFilter(request, response);
                return;
            }

            // 특정 경로에 대해 토큰 검증 우회
            if ("/api/members/adminToken".equals(requestUri)) {
                log.debug("Skipping token validation for: {}", requestUri);
                filterChain.doFilter(request, response);
                return; // 이후 코드는 실행하지 않음
            }

            // 기존 토큰 처리 로직
            String token = tokenProvider.getAccessTokenFromRequest(request);
            if (StringUtils.hasText(token) && jwtUtil.validateToken(token) == JWTValType.VALID_JWT) {
                Integer userId = jwtUtil.getUserFromJwt(token);
                Member member = memberRepository.findUserByMemberId(userId).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

                PrincipalDetails principalDetails = new PrincipalDetails(member);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        principalDetails, null, principalDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            log.error("Exception during JWT processing: ", exception);
            log.error(exception.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // 사용자 아이디 추출
    private Integer getUserId(String token) {
        return jwtUtil.getUserFromJwt(token);
    }

}