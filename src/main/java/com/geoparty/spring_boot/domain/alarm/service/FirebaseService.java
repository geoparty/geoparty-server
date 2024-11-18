package com.geoparty.spring_boot.domain.alarm.service;

import com.geoparty.spring_boot.domain.alarm.dto.TargetTokenRequest;
import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.member.service.MemberService;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FirebaseService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateTargetToken(TargetTokenRequest request) {
        Member member = memberRepository.findUserByMemberId(request.memberId())
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
        member.updateTargetToken(request.targetToken());
    }
}