package com.geoparty.spring_boot.domain.member.entity;

import com.geoparty.spring_boot.global.domain.AuditingFields;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Entity
public class Member extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer userId;

    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;

    @Setter @Column(length = 250) private String userRefreshtoken;

    @Setter @Column(nullable = false)
    @ColumnDefault("false")
    private boolean userIsWithdraw;

    @Setter
    @Column(nullable = false)
    private String socialId;

    protected Member() {}

    @Builder
    private Member(Integer userId, String email, String nickname, String userRefreshtoken, boolean userIsWithdraw, String socialId) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.userRefreshtoken = userRefreshtoken;
        this.userIsWithdraw = userIsWithdraw;
        this.socialId = socialId;
    }

    public static Member of(Integer userId, String email, String nickname, String userRefreshtoken, boolean userIsWithdraw, String socialId) {
        return new Member(userId, email, nickname, userRefreshtoken, userIsWithdraw, socialId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member that)) return false;
        return this.getUserId() != null && this.getUserId().equals(that.getUserId());
    }

    public void updateRefreshToken(String refreshToken) {
        this.userRefreshtoken = refreshToken;
    }

    public void resetRefreshToken() {
        this.userRefreshtoken = null;
    }

}