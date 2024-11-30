package com.geoparty.spring_boot.domain.member.controller;

import com.geoparty.spring_boot.domain.member.dto.MemberResponse;
import com.geoparty.spring_boot.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    @GetMapping("/api/members")
    @Operation(description = "어드민페이지에서 모든 파티 리스트를 반환한다.")
    public ResponseEntity<List<MemberResponse>> getAllParties() {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getAllMembers());
    }

    @PostMapping("/api/adminToken")
    public String getAccessToken(@RequestParam String password) {
        // 비밀번호 검증 후 토큰을 생성
        return memberService.generateToken(password);
    }
}
