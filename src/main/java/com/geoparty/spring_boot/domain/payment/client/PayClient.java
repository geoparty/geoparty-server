package com.geoparty.spring_boot.domain.payment.client;

import com.geoparty.spring_boot.domain.payment.dto.request.KakaopayApproveRequest;
import com.geoparty.spring_boot.domain.payment.dto.request.KakaopayReadyRequest;
import com.geoparty.spring_boot.domain.payment.dto.response.KakaopayReadyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="payClient", url="https://open-api.kakaopay.com")
public interface PayClient {

    @PostMapping(value="/online/v1/payment/ready", headers = {"Authorization=SECRET_KEY DEV3C55C3F3BB691FDFCBDB1D4683A3494AB2DD7"
            ,"Content-Type=application/json"})
    ResponseEntity<KakaopayReadyResponse> makePayment(@RequestBody KakaopayReadyRequest request);


    @PostMapping(value="/online/v1/payment/approve", headers = {"Authorization=SECRET_KEY DEV3C55C3F3BB691FDFCBDB1D4683A3494AB2DD7"
            ,"Content-Type=application/json"})
    ResponseEntity<KakaopayReadyResponse> savePayment(@RequestBody KakaopayApproveRequest kakaopayReadyRequestDto);
}
