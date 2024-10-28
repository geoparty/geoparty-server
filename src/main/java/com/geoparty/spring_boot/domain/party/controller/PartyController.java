package com.geoparty.spring_boot.domain.party.controller;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.party.dto.request.PartyRequest;
import com.geoparty.spring_boot.domain.party.dto.response.PartyResponse;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.service.PartyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/parties")
public class PartyController {

    private final PartyService partyService;

    @PostMapping
    @Operation(description = "파티를 생성한다.") // to-do: 로그인한 유저 정보 반환하기
    public ResponseEntity<String> createParty(@RequestBody final PartyRequest request,
                                                Member loginUser) {
        partyService.createParty(request, loginUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("파티 생성이 완료되었습니다.");
    }

    @GetMapping("/home")
    @Operation(description = "홈화면에서 로그인한 유저의 파티 리스트를 반환한다.") // to-do: 로그인한 유저 정보 반환하기
    public ResponseEntity<List<PartyResponse>> getParties(Member loginUser){
        return ResponseEntity.status(HttpStatus.OK).body(partyService.getParties(loginUser));
    }
}
