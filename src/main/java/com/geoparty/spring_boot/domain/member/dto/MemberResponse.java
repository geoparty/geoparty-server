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
    private String nickName;
//    private String email;
    private String thumbnailImageUrl;
    private String userRefreshtoken;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public static MemberResponse from(Member entity) {
        return new MemberResponse(
                entity.getMemberId(),
                entity.getNickname(),
                entity.getThumbnailImageUrl(),
                entity.getUserRefreshtoken(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

}