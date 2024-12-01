package com.geoparty.spring_boot.domain.payment.controller;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.party.dto.response.PartyDetailResponse;
import com.geoparty.spring_boot.domain.party.service.PartyService;
import com.geoparty.spring_boot.domain.payment.dto.response.*;
import com.geoparty.spring_boot.domain.payment.service.PointService;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.model.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/payments")
public class PaymentController {

    private final MemberRepository memberRepository;
    private final PartyService partyService;
    private final PointService pointService;

    @PostMapping
    @Operation(description = "카카오 페이 결제 준비.")
    public ResponseEntity<ReadyInfoResponse> readyPay(
            @AuthenticationPrincipal final PrincipalDetails details, @RequestParam final Integer point) {
        ReadyInfoResponse response = pointService.readyKakao(details, point);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/complete")
    @Operation(description = "카카오 페이 결제 진행")
    public ResponseEntity<KakaopayApproveResponse> successPay(@AuthenticationPrincipal PrincipalDetails details, @RequestParam("pg_token") String pgToken, @RequestParam("tid") String tid)
    {

        ResponseEntity<KakaopayApproveResponse> sendData = pointService.completeKakao(details, pgToken, tid);
        log.info("complete controller test"+sendData.getBody().toString());
        return sendData;
    }

    @GetMapping("/success")
    @Operation(description = "카카오 페이 결제 화면 받기")
    public void takePay(@RequestParam("pg_token") String pgToken, HttpServletResponse response)
    throws IOException {

        response.sendRedirect("geoparty://payment-complete/success?pg_token=" + pgToken);
    };


    @GetMapping("/{member-id}")
    @Operation(description = "어드민페이지에서 해당 유저의 결제 목록을 반환한다.")
    public ResponseEntity<List<UserPaymentResponse>> getAdminParties(@PathVariable(name = "member-id") Long memberId){
        Member member = memberRepository.findUserByMemberId(Math.toIntExact(memberId))
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
        return ResponseEntity.status(HttpStatus.OK).body(partyService.getPaymentsByMember(member));
    }

    @GetMapping("/point/{member-id}")
    @Operation(description = "특정 유저의 포인트 충전 목록을 반환한다.")
    public ResponseEntity<List<UserPointLogResponse>> getUserPoints(@PathVariable(name = "member-id") Long memberId){
        Member member = memberRepository.findUserByMemberId(Math.toIntExact(memberId))
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
        return ResponseEntity.status(HttpStatus.OK).body(pointService.getPointChargeLogByMember(member));
    }

    @GetMapping
    @Operation(description = "모든 파티의 후원 내역을 반환한다.")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.status(HttpStatus.OK).body(partyService.toAllPaymentResponse());
    }

    @PutMapping("/{payment-id}/complete")
    @Operation(description = "특정 후원 내역의 후원 처리 여부를 업데이트한다.")
    public ResponseEntity<String> updatePaymentCompleted(@PathVariable(name = "payment-id") Long paymentId) {
        partyService.updatePaymentCompleted(paymentId);
        return ResponseEntity.status(HttpStatus.OK).body("후원 처리 여부가 업데이트 되었습니다.");
    }

    @PutMapping("/{payment-id}/mail")
    @Operation(description = "특정 후원 내역의 메일 처리 여부를 업데이트한다.")
    public ResponseEntity<String> updatePaymentMailed(@PathVariable(name = "payment-id") Long paymentId) {
        partyService.updatePaymentMailed(paymentId);
        return ResponseEntity.status(HttpStatus.OK).body("메일 처리 여부가 업데이트 되었습니다.");
    }

}
