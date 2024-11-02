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
    String nickName;
    String profileImage;

    public void setUserData(Map<String,Object> userData){ // Json 데이터를 매핑해주는 함수
        socialId = userData.get("id").toString();
        ObjectMapper om = new ObjectMapper();
        Map profileData = om.convertValue(userData.get("profile"), Map.class);
        nickName = profileData.get("nickname").toString();
        profileImage = profileData.get("thumbnail_image_url").toString();
    }
}
