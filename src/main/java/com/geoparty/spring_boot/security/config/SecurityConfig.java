package com.geoparty.spring_boot.security.config;

import com.geoparty.spring_boot.security.jwt.CustomJWTAuthenticationEntryPoint;
import com.geoparty.spring_boot.security.jwt.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final CustomJWTAuthenticationEntryPoint customJwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(configurer -> configurer.configurationSource(corsConfigurationSource())) // CORS 설정 추가
//                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호를 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 폼 기반 로그인을 비활성화
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 기반 인증을 사용하지 않음
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(customJwtAuthenticationEntryPoint)) // 인증 실패 시 customJwtAuthenticationEntryPoint에서 처리
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/api/payments/success").permitAll()
                                .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/adminToken/**").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터를 UsernamePasswordAuthenticationFilter 전에 추가
                .build();
    }

    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOriginPatterns(Collections.singletonList("*"));
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowCredentials(true); // 자격 증명 허용
            config.addExposedHeader("Access-Control-Allow-Origin"); // CORS 허용 헤더 노출
            config.addExposedHeader("Access-Control-Allow-Credentials"); // CORS 자격증명 허용 헤더 노출
            return config;
        };
    }
}
