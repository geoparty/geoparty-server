package com.geoparty.spring_boot.domain.member.repository;

import com.geoparty.spring_boot.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findBySocialId(String socialId); // 소셜 로그인

    Optional<Member> findUserByUserId(int userId);
}