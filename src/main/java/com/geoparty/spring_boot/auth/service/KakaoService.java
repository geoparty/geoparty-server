package com.geoparty.spring_boot.auth.service;


import com.geoparty.spring_boot.auth.client.KakaoAuthApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
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

    // 안드로이드는 이걸 os가 해서 넘겨줌.
//    public String getToken(String code){
//        return kakaoAuthApi.getAccessToken(clientId,clientSecret,grantType,redirectUri, code).getBody();
//    }

    public Map<String, String> getKakaoData(String socialAccessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", socialAccessToken);
            HttpEntity<JsonArray> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<Object> responseData = restTemplate.postForEntity("https://kapi.kakao.com/v2/user/me", httpEntity, Object.class);
            Map<String,String> result = objectMapper.convertValue(responseData.getBody(), Map.class); // json 추출
            Map<String, String> data = objectMapper.convertValue(result.get("kakao_account"), Map.class); // 계정 정보 추출
            data.put("id", String.valueOf(result.get("id")));
            log.info(data.toString());
            return data;
        } catch (Exception exception) {
            log.error(Arrays.toString(exception.getStackTrace()));
            throw new IllegalStateException();
        }
    }
}
