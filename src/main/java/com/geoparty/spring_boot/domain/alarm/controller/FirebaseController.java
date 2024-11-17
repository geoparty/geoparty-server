package com.geoparty.spring_boot.domain.alarm.controller;

import com.geoparty.spring_boot.domain.alarm.dto.FcmMessage;
import com.geoparty.spring_boot.domain.alarm.dto.TargetTokenRequest;
import com.geoparty.spring_boot.domain.alarm.service.FirebaseService;
import com.geoparty.spring_boot.security.model.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/fcm")
public class FirebaseController {

    private final FirebaseService firebaseService;

    @PutMapping("/targetToken")
    @Operation(description = "fcm token을 업데이트한다.")

    public ResponseEntity<String> updateTargetToken(@RequestBody final TargetTokenRequest request,
                                                    @AuthenticationPrincipal final PrincipalDetails details) {

        firebaseService.updateTargetToken(request.targetToken(), details.getMember());
        return ResponseEntity.status(HttpStatus.CREATED).body("유저의 fcm token이 업데이트 되었습니다.");
    }


    @PostMapping("/sendMessage")
    @Operation(description = "서버 fcm 테스트용")
    public ResponseEntity<String> sendMessage(@RequestBody FcmMessage message) {
        String response = firebaseService.sendFCMMessage(message.getTargetToken(), message.getTitle(), message.getContent());
        return ResponseEntity.ok(response);
    }


}