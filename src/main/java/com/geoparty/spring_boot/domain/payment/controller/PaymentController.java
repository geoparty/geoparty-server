package com.geoparty.spring_boot.domain.payment.controller;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.party.service.PartyService;
import com.geoparty.spring_boot.domain.payment.dto.response.UserPaymentResponse;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/payments")
public class PaymentController {

    private final MemberRepository memberRepository;
    private final PartyService partyService;

    @GetMapping("/{member-id}")
    @Operation(description = "어드민페이지에서 해당 유저의 결제 목록을 반환한다.")
    public ResponseEntity<List<UserPaymentResponse>> getAdminParties(@PathVariable(name = "member-id") Long memberId){
        Member member = memberRepository.findUserByMemberId(Math.toIntExact(memberId))
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
        return ResponseEntity.status(HttpStatus.OK).body(partyService.getPaymentsByMember(member));
    }
}
