package com.geoparty.spring_boot.domain.party.controller;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.party.dto.request.PartyRequest;
import com.geoparty.spring_boot.domain.party.dto.response.PartyDetailResponse;
import com.geoparty.spring_boot.domain.party.dto.response.PartyResponse;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.service.PartyService;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.model.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/parties")
public class PartyController {

    private final PartyService partyService;

    @PostMapping
    @Operation(description = "파티를 생성한다.")
    public ResponseEntity<String> createParty(@RequestBody final PartyRequest request,
                                              @AuthenticationPrincipal final PrincipalDetails details) {
//        Member member = Member.builder() // 테스트용 멤버
//                .nickname("exampleNickname")
//                .userRefreshtoken("someRefreshToken")
//                .userIsWithdraw(false)
//                .socialId("socialId123")
//                .build();
//        memberRepository.save(member);
        partyService.createParty(request, details.getMember());
        return ResponseEntity.status(HttpStatus.CREATED).body("파티 생성이 완료되었습니다.");
    }

    @GetMapping("/home")
    @Operation(description = "홈화면에서 로그인한 유저의 파티 리스트를 반환한다.")
    public ResponseEntity<List<PartyResponse>> getHomeParties(@AuthenticationPrincipal final PrincipalDetails details){
        return ResponseEntity.status(HttpStatus.OK).body(partyService.getHomeParties(details.getMember()));
    }

    @GetMapping
    @Operation(description = "환경단체에 따른 파티 리스트를 반환한다.")
    public ResponseEntity<List<PartyResponse>> getParties(
            @RequestParam(name = "organization-id", required = false) Long organizationId,
            @RequestParam(name = "party-name", required = false) String partyName){

        if (organizationId != null) {
            // 특정 환경 단체의 파티 조회
            return ResponseEntity.status(HttpStatus.OK).body(partyService.getPartiesByOrganization(organizationId));
        } else {
            // 파티 이름에 따른 파티 조회
            return ResponseEntity.status(HttpStatus.OK).body(partyService.getPartiesByName(partyName));
        }
    }

    @GetMapping("/{party-id}")
    @Operation(description = "파티 세부 정보를 반환한다")
    public ResponseEntity<PartyDetailResponse> getPartyDetails(
            @PathVariable(name = "party-id") Long partyId) {
        return ResponseEntity.status(HttpStatus.OK).body(partyService.getPartyDetails(partyId));
    }
}
