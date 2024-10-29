package com.geoparty.spring_boot.domain.member.dto;

import com.geoparty.spring_boot.domain.member.entity.Member;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@Builder
public class MemberResponse {
    private Integer memberId;
    private String email;
    private String nickname;
    private String userRefreshtoken;
    private String socialId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public static MemberResponse from(Member entity) {
        return new MemberResponse(
                entity.getMemberId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getUserRefreshtoken(),
                entity.getSocialId(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

}