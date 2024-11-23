package com.geoparty.spring_boot.domain.payment.service;

import com.geoparty.spring_boot.domain.payment.client.PayClient;
import com.geoparty.spring_boot.domain.payment.dto.request.KakaopayApproveRequest;
import com.geoparty.spring_boot.domain.payment.dto.request.KakaopayReadyRequest;
import com.geoparty.spring_boot.domain.payment.dto.response.KakaopayReadyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaopayService {

    private final PayClient payClient;

    public ResponseEntity<KakaopayReadyResponse> sendApprove(KakaopayReadyRequest request) {
        return payClient.makePayment(request);
    }

    public ResponseEntity<?> sendComplete(KakaopayApproveRequest request) {
        return payClient.savePayment(request);
    }
}
