package com.geoparty.spring_boot.domain.user.repository;

import com.geoparty.spring_boot.domain.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    Optional<UserAccount> findBySocialId(String socialId); // 소셜 로그인

    Optional<UserAccount> findUserByUserId(int userId);
}