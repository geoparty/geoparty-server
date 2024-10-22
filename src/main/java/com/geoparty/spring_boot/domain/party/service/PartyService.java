package com.geoparty.spring_boot.domain.party.service;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.party.dto.request.PartyRequest;
import com.geoparty.spring_boot.domain.party.dto.response.PartyResponse;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.entity.UserParty;
import com.geoparty.spring_boot.domain.party.repository.PartyRepository;
import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.party.repository.UserPartyRepository;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final UserPartyRepository userPartyRepository;

    @Transactional
    public void createParty(PartyRequest request, Member user) {
//        validMember(user);
        String imgUrl = "환경 단체 이미지 url"; // to-do: 환경 단체 이미지 url로 변경
        Party party = request.toEntity(imgUrl, user);
        partyRepository.save(party);
        createUserParty(user, party);
    }

    public void validMember(Member member){
        Member loginUser = memberRepository.findUserByUserId(member.getUserId())
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public void createUserParty(Member member, Party party) {
        UserParty userParty = UserParty.builder()
                .member(member)
                .party(party)
                .build();
        userPartyRepository.save(userParty);
    }

    public List<PartyResponse> getParties(Member loginMember) {
//        validMember(loginMember);
        List<Party> parties = userPartyRepository.findPartiesByUserId(loginMember.getUserId());
        return parties.stream()
                .map(party -> PartyResponse.from(party))
                .collect(Collectors.toList());
    }
}
