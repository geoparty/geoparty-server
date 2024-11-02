package com.geoparty.spring_boot.domain.member.service;

import com.geoparty.spring_boot.domain.member.dto.MemberResponse;

public interface MemberService {
    MemberResponse getUserInfo(String accessToken);
}
