package com.geoparty.spring_boot.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            val token = tokenProvider.getAccessTokenFromRequest(request);
            if (StringUtils.hasText(token) && jwtUtil.validateToken(token) == JWTValType.VALID_JWT) { // null 이 아니고 유효하다면
                val authentication = new UserAuthentication(getUserId(token), null, null); // 사용자의 식별자를 추출하지
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // 사용자 아이디 추출
    private Integer getUserId(String token) {
        return jwtUtil.getUserFromJwt(token);
    }

}