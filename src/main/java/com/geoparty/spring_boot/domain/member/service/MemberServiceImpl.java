package com.geoparty.spring_boot.domain.member.service;

import com.geoparty.spring_boot.domain.member.dto.MemberResponse;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @Override
    public MemberResponse getUserInfo(String accessToken) {

        Integer userId = jwtUtil.getUserFromJwt(accessToken);
        return MemberResponse.from(memberRepository.findUserByUserId(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA)));
    }

}
