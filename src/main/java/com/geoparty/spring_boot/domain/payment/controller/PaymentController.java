package com.geoparty.spring_boot.domain.payment.controller;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.party.service.PartyService;
import com.geoparty.spring_boot.domain.payment.dto.response.UserPaymentResponse;
import com.geoparty.spring_boot.domain.payment.service.KakaopayService;
import com.geoparty.spring_boot.domain.payment.service.PaymentService;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.domain.party.dto.request.PartyRequest;
import com.geoparty.spring_boot.security.model.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/payments")
public class PaymentController {
  
    private final MemberRepository memberRepository;
    private final PartyService partyService;
    private final KakaopayService kakaoService;
    private final PaymentService paymentService;

    @PostMapping
    @Operation(description = "카카오 페이 결제 준비.")
    public ResponseEntity<String> readyPay(
                                              @AuthenticationPrincipal final PrincipalDetails details) {
        String webURL = paymentService.readyKakao(details);
        log.info(webURL);
        return ResponseEntity.status(HttpStatus.OK).body(webURL);
    }
    
    @GetMapping("/{member-id}")
    @Operation(description = "어드민페이지에서 해당 유저의 결제 목록을 반환한다.")
    public ResponseEntity<List<UserPaymentResponse>> getAdminParties(@PathVariable(name = "member-id") Long memberId){
        Member member = memberRepository.findUserByMemberId(Math.toIntExact(memberId))
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
        return ResponseEntity.status(HttpStatus.OK).body(partyService.getPaymentsByMember(member));
    }
  
}
