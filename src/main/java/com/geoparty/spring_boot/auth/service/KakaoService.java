package com.geoparty.spring_boot.auth.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.geoparty.spring_boot.auth.client.KakaoAuthApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geoparty.spring_boot.auth.dto.KakaoUserData;
import com.google.gson.JsonArray;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final KakaoAuthApi kakaoAuthApi;

    @Value(value = "${jwt.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value(value = "${jwt.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value(value = "${jwt.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value(value = "${jwt.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;

//    public String getToken(String code){
//        return kakaoAuthApi.getAccessToken(clientId,clientSecret,grantType,redirectUri, code).getBody();
//    }

    public Map<String, Object> getKakaoData(String socialAccessToken) throws JsonProcessingException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + socialAccessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoUserInfoRequest,
                    String.class
            );
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            responseBody = response.getBody();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            jsonNode = objectMapper.readTree(responseBody);

            // 필요한 데이터를 Map에 담기
            Map<String, Object> userInfo = new HashMap<>();
            if (jsonNode.has("id")) {
                userInfo.put("id", jsonNode.get("id").asText());
            }

            // 이메일 주소 담기
            if(jsonNode.has("kakao_account") && jsonNode.get("kakao_account").has("email")) {
                userInfo.put("email", jsonNode.get("kakao_account").get("email").asText());
            }

            // profile 데이터를 별도의 Map으로 저장
            Map<String, String> profileData = new HashMap<>();
            if (jsonNode.has("kakao_account") && jsonNode.get("kakao_account").has("profile")) {
                JsonNode profileNode = jsonNode.get("kakao_account").get("profile");
                if (profileNode.has("nickname")) {
                    profileData.put("nickname", profileNode.get("nickname").asText());
                }
                if (profileNode.has("thumbnail_image_url")) {
                    profileData.put("thumbnail_image_url", profileNode.get("thumbnail_image_url").asText());
                }
            }
            userInfo.put("profile", profileData); // profile 데이터를 userInfo에 추가

            log.info("카카오 사용자 데이터: {}", userInfo);
            return userInfo;
        } catch (Exception exception) {
            log.error("카카오 데이터 가져오기 중 오류: " + exception.getMessage(), exception);
            throw new IllegalStateException("카카오 데이터 처리 중 오류 발생", exception);
        }
    }
}
