package com.geoparty.spring_boot.domain.member.service;

import com.geoparty.spring_boot.domain.member.dto.MemberDto;

public interface MemberService {
    MemberDto getUserInfo(String accessToken);
}
