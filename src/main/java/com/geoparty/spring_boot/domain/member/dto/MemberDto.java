package com.geoparty.spring_boot.domain.member.dto;

import com.geoparty.spring_boot.domain.member.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberDto(
        Integer userId,
        String email,
        String nickname,
        String userRefreshtoken,
        String socialId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt

) {

    public static MemberDto from(Member entity) {
        return new MemberDto(
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