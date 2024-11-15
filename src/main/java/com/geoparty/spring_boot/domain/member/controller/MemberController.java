package com.geoparty.spring_boot.domain.member.controller;

import com.geoparty.spring_boot.domain.member.dto.MemberResponse;
import com.geoparty.spring_boot.domain.member.service.MemberServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/members")
public class MemberController {

    private final MemberServiceImpl memberService;
    @GetMapping
    @Operation(description = "어드민페이지에서 모든 파티 리스트를 반환한다.")
    public ResponseEntity<List<MemberResponse>> getAllParties() {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getAllMembers());
    }
}
