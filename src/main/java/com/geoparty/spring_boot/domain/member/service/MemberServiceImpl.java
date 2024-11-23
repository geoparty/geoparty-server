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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @Value("${secret.password}")
    private String SECRET_PASSWORD; // 서버에 저장된 비밀번호
    private static final int ACCESS_TOKEN_EXPIRATION = 3 * 24 * 60 * 60 * 1000; // 3일


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

    public String generateToken(String password) {
        if (!password.equals(SECRET_PASSWORD)) {
            throw new IllegalArgumentException("Invalid password"); // 비밀번호가 틀리면 예외 처리
        }

        // 비밀번호가 맞으면 JWT 토큰 생성
        return jwtUtil.generateAdminToken(ACCESS_TOKEN_EXPIRATION);
    }
}
