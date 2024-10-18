package com.geoparty.spring_boot.domain.party.controller;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.party.dto.request.PartyRequest;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.service.PartyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/parties")
public class PartyController {

    private final PartyService partyService;

    @PostMapping
    @Operation(description = "파티를 생성한다.") // to-do: 로그인한 유저 정보 반환하기
    public ResponseEntity<String> createProject(@RequestBody final PartyRequest request,
                                                Member loginUser) {
        partyService.createParty(request, loginUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("파티 생성이 완료되었습니다.");
    }

}
