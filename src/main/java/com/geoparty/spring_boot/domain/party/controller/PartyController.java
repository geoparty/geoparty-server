package com.geoparty.spring_boot.domain.party.controller;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.party.dto.request.PartyRequest;
import com.geoparty.spring_boot.domain.party.dto.response.PartyDetailResponse;
import com.geoparty.spring_boot.domain.party.dto.response.PartyIdResponse;
import com.geoparty.spring_boot.domain.party.dto.response.PartyResponse;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.repository.PartyRepository;
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
    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;

    @PostMapping
    @Operation(description = "파티를 생성한다.")
    public ResponseEntity<PartyIdResponse> createParty(@RequestBody final PartyRequest request,
                                                       @AuthenticationPrincipal final PrincipalDetails details) {
//        Member member = Member.builder() // 테스트용 멤버
//                .nickname("exampleNickname")
//                .userRefreshtoken("someRefreshToken")
//                .userIsWithdraw(false)
//                .socialId("socialId123")
//                .build();
//        memberRepository.save(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(partyService.createParty(request, details.getMember()));
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
        if (organizationId != null && partyName != null) {
            // 환경 단체 ID와 파티 이름 모두 존재하는 경우
            return ResponseEntity.status(HttpStatus.OK).body(
                    partyService.getPartiesByOrganizationAndName(organizationId, partyName)
            );
        } else if (organizationId != null) {
            // 환경 단체 ID만 존재하는 경우
            return ResponseEntity.status(HttpStatus.OK).body(partyService.getPartiesByOrganization(organizationId));
        } else if (partyName != null) {
            // 파티 이름만 존재하는 경우
            return ResponseEntity.status(HttpStatus.OK).body(partyService.getPartiesByName(partyName));
        } else {
            // 아무 조건도 없는 경우 모든 파티 반환
            return ResponseEntity.status(HttpStatus.OK).body(partyService.getAllParties());
        }
    }

    @GetMapping("/{party-id}")
    @Operation(description = "파티 세부 정보를 반환한다")
    public ResponseEntity<PartyDetailResponse> getPartyDetails(
            @PathVariable(name = "party-id") Long partyId) {
        return ResponseEntity.status(HttpStatus.OK).body(partyService.getPartyDetails(partyId));
    }

    @PostMapping("/{party-id}/invitations")
    @Operation(description = "파티에 로그인한 유저를 추가한다")
    public ResponseEntity<String> createUserParty(@PathVariable(name = "party-id") Long partyId,
                                                  @RequestParam(name = "member-id") Long memberId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new BaseException(ErrorCode.PARTY_NOT_FOUND));
        Member member = memberRepository.findUserByMemberId(Math.toIntExact(memberId))
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));

        partyService.createUserParty(member, party);
        return ResponseEntity.status(HttpStatus.CREATED).body("파티에 유저가 추가되었습니다.");
    }

    @GetMapping("/admin")
    @Operation(description = "어드민페이지에서 모든 파티 리스트를 반환한다.")
    public ResponseEntity<List<PartyResponse>> getAllParties() {
            return ResponseEntity.status(HttpStatus.OK).body(partyService.getAllParties());
    }

    @GetMapping("/admin/{member-id}")
    @Operation(description = "어드민페이지에서 해당 유저가 속한 모든 파티 리스트를 반환한다.")
    public ResponseEntity<List<PartyResponse>> getAdminParties(@PathVariable(name = "member-id") Long memberId){
        Member member = memberRepository.findUserByMemberId(Math.toIntExact(memberId))
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
        return ResponseEntity.status(HttpStatus.OK).body(partyService.getHomeParties(member));
    }

}
