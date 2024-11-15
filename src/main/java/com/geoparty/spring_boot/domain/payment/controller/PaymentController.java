package com.geoparty.spring_boot.domain.payment.controller;

import com.geoparty.spring_boot.domain.party.dto.request.PartyRequest;
import com.geoparty.spring_boot.security.model.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/pay")
public class PaymentController {

    @PostMapping
    @Operation(description = "카카오 페이 결제 준비.")
    public ResponseEntity<String> readyForPay(@RequestBody final PartyRequest request,
                                              @AuthenticationPrincipal final PrincipalDetails details) {


        return ResponseEntity.status(HttpStatus.CREATED).body("결제 api 호출 완료");
    }
}
