package com.geoparty.spring_boot.auth.service;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.dto.MemberDto;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)

    public MemberDto saveUser(Integer username, String email, String nickname, String userRefreshtoken, boolean userIsWithdraw, String socialId) {
        return MemberDto.from(
                memberRepository.save(Member.of(username, email, nickname, userRefreshtoken,userIsWithdraw,socialId))
        );
    }

}