package com.geoparty.spring_boot.auth.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaoUserData {
    String socialId;
    String email;
    String gender;
    String birthYear;
    String nickName;
    String profileImage;

    public void setUserData(Map<String,String> userData){ // Json 데이터를 매핑해주는 함수
        socialId = userData.get("id");
        email = userData.get("email");
        gender = userData.get("gender");
        birthYear = userData.get("birthyear");
        ObjectMapper om = new ObjectMapper();
        Map profileData = om.convertValue(userData.get("profile"), Map.class);
        nickName = profileData.get("nickname").toString();
        profileImage = profileData.get("thumbnail_image_url").toString();
    }
}
