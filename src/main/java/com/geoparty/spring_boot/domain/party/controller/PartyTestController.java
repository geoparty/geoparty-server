package com.geoparty.spring_boot.domain.party.controller;

import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.party.repository.PartyRepository;
import com.geoparty.spring_boot.domain.party.service.PartyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/test")
public class PartyTestController {

    private final PartyService partyService;

    @PostMapping("/executePartyLogic")
    @Operation(description = "파티 메인 로직 실행")
    public ResponseEntity<String> executePartyLogic() {
        partyService.checkPayDateAndExecute();
        return ResponseEntity.status(HttpStatus.CREATED).body("파티 상태가 업데이트되었습니다.");
    }

    @PutMapping ("/payDateToToday/{party-id}")
    @Operation(description = "파티의 정기결제일을 오늘로 변경")
    public ResponseEntity<String> todayIsPayDate(@PathVariable(name = "party-id") Long partyId) {
        partyService.todayIsPayDate(partyId);
        return ResponseEntity.status(HttpStatus.CREATED).body("해당 파티의 정기결제일이 오늘로 변경되었습니다.");
    }

    @DeleteMapping ("/deletePartyMember/{party-id}/{member-id}")
    @Operation(description = "특정 파티에서 특정 멤버 삭제")
    public ResponseEntity<String> deletePartyMember(@PathVariable(name = "party-id") Long partyId,
                                                 @PathVariable(name = "member-id") Integer memberId) {
        partyService.deletePartyMember(partyId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body("해당 파티에서 해당 멤버가 삭제되었습니다.");
    }
}
