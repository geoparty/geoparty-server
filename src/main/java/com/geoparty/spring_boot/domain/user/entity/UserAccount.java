package com.geoparty.spring_boot.domain.user.entity;

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
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingFields {
    @Id
    @Column(length = 50)
    private Integer userId;

    @Setter @Column(nullable = false) private String userPassword;

    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;

    @Setter @Column(length = 250) private String userRefreshtoken;

    @Setter @Column(nullable = false)
    @ColumnDefault("false")
    private boolean userIsWithdraw;

    @Setter
    @Column(nullable = false)
    private String socialId;

    protected UserAccount() {}

    @Builder
    private UserAccount(Integer userId, String userPassword, String email, String nickname, String userRefreshtoken, boolean userIsWithdraw, String socialId) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.userRefreshtoken = userRefreshtoken;
        this.userIsWithdraw = userIsWithdraw;
        this.socialId = socialId;
    }

    public static UserAccount of(Integer userId, String userPassword, String email, String nickname, String userRefreshtoken, boolean userIsWithdraw, String socialId) {
        return new UserAccount(userId, userPassword, email, nickname, userRefreshtoken, userIsWithdraw, socialId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return this.getUserId() != null && this.getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }

    public void updateRefreshToken(String refreshToken) {
        this.userRefreshtoken = refreshToken;
    }

    public void resetRefreshToken() {
        this.userRefreshtoken = null;
    }

}