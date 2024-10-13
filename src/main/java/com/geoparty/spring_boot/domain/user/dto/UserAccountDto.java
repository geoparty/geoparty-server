package com.geoparty.spring_boot.domain.user.dto;

import com.geoparty.spring_boot.domain.user.entity.UserAccount;

import java.time.LocalDateTime;

public record UserAccountDto(
        Integer userId,
//        String userPassword,
        String email,
        String nickname,
        String userRefrashtoken,
        String socialId,
        LocalDateTime createdAt,

        LocalDateTime modifiedAt

) {

    public static UserAccountDto of(Integer userId, String email, String nickname, String userRefrashtoken, String socialId ,LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new UserAccountDto(userId, email, nickname, userRefrashtoken, socialId, createdAt, modifiedAt);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getUserId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getUserRefreshtoken(),
                entity.getSocialId(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

}