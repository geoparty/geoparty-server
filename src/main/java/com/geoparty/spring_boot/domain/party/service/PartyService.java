package com.geoparty.spring_boot.domain.party.service;

import com.geoparty.spring_boot.domain.member.dto.MemberDto;
import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.organization.repository.OrganizationRepository;
import com.geoparty.spring_boot.domain.party.dto.request.PartyRequest;
import com.geoparty.spring_boot.domain.party.dto.response.PartyDetailResponse;
import com.geoparty.spring_boot.domain.party.dto.response.PartyResponse;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.entity.UserParty;
import com.geoparty.spring_boot.domain.party.repository.PartyRepository;
import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.party.repository.UserPartyRepository;
import com.geoparty.spring_boot.domain.payment.dto.response.PaymentResponse;
import com.geoparty.spring_boot.domain.payment.entity.Payment;
import com.geoparty.spring_boot.domain.payment.repository.PaymentRepository;
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
    private final OrganizationRepository organizationRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void createParty(PartyRequest request, Member user) {
//        validMember(user);
        Party party = request.toEntity(user, countPartyMembers(request.getTargetPoint(), request.getPointPerPerson()));
        partyRepository.save(party);
        createUserParty(user, party);
    }

    public Integer countPartyMembers(Integer targetPoint, Integer pointPerPerson){
        if (pointPerPerson == 0) {
            throw new IllegalArgumentException("1인당 후원 금액은 0이 될 수 없습니다.");
        }
        return (int) Math.ceil((double) targetPoint / pointPerPerson);
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

    public List<PartyResponse> getHomeParties(Member loginMember) {
//        validMember(loginMember);
        List<UserParty> parties = userPartyRepository.findUserPartiesByMember(loginMember);
        return parties.stream()
                .map(userParty -> PartyResponse.from(userParty.getParty(), userParty.getParty().getOrganization()))
                .collect(Collectors.toList());
    }

    public List<PartyResponse> getPartiesByOrganization(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new BaseException(ErrorCode.ORGANIZATION_NOT_FOUND));
        List<Party> parties = partyRepository.findByOrganization(organization);
        return parties.stream()
                .map(party -> PartyResponse.from(party, organization))
                .collect(Collectors.toList());
    }

    public List<PartyResponse> getPartiesByName(String partyName) {
        List<Party> parties = partyRepository.findByTitle(partyName);
        return parties.stream()
                .map(party -> PartyResponse.from(party, party.getOrganization()))
                .collect(Collectors.toList());
    }

    public PartyDetailResponse getPartyDetails(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new BaseException(ErrorCode.PARTY_NOT_FOUND));
        return PartyDetailResponse.from(PartyResponse.from(party, party.getOrganization()), toMemberResponse(party), toPaymentResponse(party));
    }

    public List<MemberDto> toMemberResponse(Party party) {
        List<UserParty> members = userPartyRepository.findUserPartiesByParty(party);
        return members.stream()
                .map(userParty -> MemberDto.from(userParty.getMember()))
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> toPaymentResponse(Party party) {
        List<Payment> payments = paymentRepository.findAllByPartyId(party.getId());
        return payments.stream()
                .map(payment -> PaymentResponse.from(payment))
                .collect(Collectors.toList());
    }
}
