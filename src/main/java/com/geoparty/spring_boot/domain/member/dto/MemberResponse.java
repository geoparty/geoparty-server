package com.geoparty.spring_boot.domain.member.dto;

import com.geoparty.spring_boot.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class MemberResponse {

    private Integer memberId;
    private String email;
    private String nickName;
    private String thumbnailImageUrl;


    @Builder
    public MemberResponse(Integer memberId, String email, String nickName, String thumbnailImageUrl, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.memberId = memberId;
        this.email = email;
        this.nickName = nickName;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    @Builder
    public static MemberResponse from(Member entity) {
        return new MemberResponse().builder()
                .memberId(entity.getMemberId())
                .email(entity.getEmail())
                .nickName(entity.getNickname())
                .thumbnailImageUrl(entity.getThumbnailImageUrl())
                .build();

    }

}