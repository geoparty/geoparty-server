package com.geoparty.spring_boot.domain.user.dto;

import com.geoparty.spring_boot.domain.user.entity.UserAccount;

import java.time.LocalDateTime;

public record UserAccountDto(
        Integer userId,
        String userPassword,
        String email,
        String nickname,
        String userRefrashtoken,
        String socialId,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static UserAccountDto of(Integer userId, String userPassword, String email, String nickname, String userRefrashtoken, String socialId ,LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new UserAccountDto(userId, userPassword, email, nickname, userRefrashtoken, socialId, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getUserId(),
                entity.getUserPassword(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getUserRefreshtoken(),
                entity.getSocialId(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

}