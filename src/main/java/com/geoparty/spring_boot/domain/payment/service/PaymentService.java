package com.geoparty.spring_boot.domain.payment.service;

import com.geoparty.spring_boot.domain.payment.dto.request.KakaopayReadyRequest;
import com.geoparty.spring_boot.domain.payment.dto.response.KakaopayReadyResponse;
import com.geoparty.spring_boot.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KakaopayService kakaopayService;

    public String readyKakao(PrincipalDetails details) {
        Integer memberId = details.getMember().getMemberId();
        ResponseEntity<KakaopayReadyResponse> payApprove = kakaopayService.sendApprove(
                KakaopayReadyRequest.builder()
                        .cid("TC0ONETIME")
                        .partner_order_id("partner_order_id")
                        .partner_user_id(String.valueOf(memberId))
                        .item_name("mileage")
                        .quantity("1")
                        .total_amount("2200")
                        .tax_free_amount("0")
                        .approval_url("https://developers.kakao.com/success")
                        .cancel_url("https://developers.kakao.com/cancel")
                        .fail_url("https://developers.kakao.com/fail").build());
        String tid = Objects.requireNonNull(payApprove.getBody()).getTid();
        //TODO : db랑 데이터 맞추는 로직
        String webURL = payApprove.getBody().getNextRedirectMobileUrl();
        return webURL;

    }
}
