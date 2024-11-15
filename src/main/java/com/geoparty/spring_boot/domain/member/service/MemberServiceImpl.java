package com.geoparty.spring_boot.domain.member.service;

import com.geoparty.spring_boot.domain.member.dto.MemberResponse;
import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.party.dto.response.PartyResponse;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @Override
    public MemberResponse getUserInfo(String accessToken) {

        Integer memberId = jwtUtil.getUserFromJwt(accessToken);
        return MemberResponse.from(memberRepository.findUserByMemberId(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA)));
    }


    public List<MemberResponse> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(member -> MemberResponse.from(member))
                .collect(Collectors.toList());
    }

}
