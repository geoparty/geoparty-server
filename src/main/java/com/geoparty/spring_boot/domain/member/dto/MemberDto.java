package com.geoparty.spring_boot.domain.member.dto;

import com.geoparty.spring_boot.domain.member.entity.Member;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@Builder
public class MemberDto implements UserDetails{
        private Integer memberId;
        private String email;
        private String nickname;
        private String userRefreshtoken;
        private String socialId;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;


    public static MemberDto from(Member entity) {
        return new MemberDto(
                entity.getMemberId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getUserRefreshtoken(),
                entity.getSocialId(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한이 필요한 경우 설정 가능
    }

    @Override
    public String getPassword() {
        return null; // 비밀번호가 없으면 null 반환
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}