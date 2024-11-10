package com.geoparty.spring_boot.domain.party.service;

import com.geoparty.spring_boot.domain.member.dto.MemberResponse;
import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.organization.repository.OrganizationRepository;
import com.geoparty.spring_boot.domain.party.dto.request.PartyRequest;
import com.geoparty.spring_boot.domain.party.dto.response.PartyDetailResponse;
import com.geoparty.spring_boot.domain.party.dto.response.PartyResponse;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.entity.PartyType;
import com.geoparty.spring_boot.domain.party.entity.UserParty;
import com.geoparty.spring_boot.domain.party.repository.PartyRepository;
import com.geoparty.spring_boot.domain.party.repository.UserPartyRepository;
import com.geoparty.spring_boot.domain.payment.dto.response.PaymentResponse;
import com.geoparty.spring_boot.domain.payment.entity.Payment;
import com.geoparty.spring_boot.domain.payment.entity.UserPayment;
import com.geoparty.spring_boot.domain.payment.repository.PaymentRepository;
import com.geoparty.spring_boot.domain.payment.repository.UserPaymentRepository;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final UserPartyRepository userPartyRepository;
    private final OrganizationRepository organizationRepository;
    private final PaymentRepository paymentRepository;
    private final UserPaymentRepository userPaymentRepository;

    @Transactional
    public void createParty(PartyRequest request, Member user) {
        validMember(user);
        Party party = request.toEntity(user);
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
        memberRepository.findUserByMemberId(member.getMemberId())
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public void createUserParty(Member member, Party party) {
        Integer count = userPartyRepository.countUserPartiesByParty(party); // 현재 파티에 몇 명 있는지
        Integer size = party.getSize(); // 파티 정원

        if (count >= size) { // 파티 인원수 초과
            throw new BaseException(ErrorCode.PARTY_IS_FULL);
        } else {
            if (userPartyRepository.existsByMemberAndParty(member, party)) { // 중복 체크
                throw new BaseException(ErrorCode.USER_ALREADY_IN_PARTY);
            }

            UserParty userParty = UserParty.builder()
                    .member(member)
                    .party(party)
                    .build();
            userPartyRepository.save(userParty);
            log.info("새로운 UserParty 생성");

                changePartyStatus(party); // 로직 A 실행 (정원==파티 인원수)
            }
        }


    // 매일 자정에 실행되는 스케줄러 (매일 00:00:00에 실행)
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkPayDateAndExecute() {
        LocalDate today = LocalDate.now();
        List<Party> partiesToPay = partyRepository.findByPayDate(today);

        // 파티에 대해 로직 A 실행
        for (Party party : partiesToPay) {
            changePartyStatus(party);
        }
    }

    // 로직 A
    public void changePartyStatus(Party party){ // 파티 정원 미달, 충족 으로 나눠서 생각하기
        Integer count = userPartyRepository.countUserPartiesByParty(party); // 현재 파티에 몇 명 있는지

        if (hasEnoughPoints(party) && Objects.equals(count, party.getSize())) { // 유저들의 포인트 충분하고  true
            createPayment(party); // 파티 결제 내역 생성
            createUserPayment(party); // 유저 결제 내역 생성

            party.updatePartyType(PartyType.B);
            party.updateDuration();
            party.updatePayDate();
            log.info("파티 타입 B로 변경");
        } else { // 유저 포인트 불만족
            if (!Objects.equals(count, party.getSize())) {
                party.resetPayDate();
                party.updatePartyType(PartyType.C);
                log.info("파티 타입 C로 변경");
            } else { // TYPE D) 멤버 중에 포인트 미달이 있어서 정기 결제를 하다가 중지된 상태
                party.updatePartyType(PartyType.D);
                party.extendPayDate();
                log.info("파티 타입 D로 변경");
                }
            }
        }

    public boolean hasEnoughPoints(Party party) {
        List<UserParty> userParties = userPartyRepository.findUserPartiesByParty(party);
        Integer requiredPoints = party.getPointPerPerson();

        for (UserParty userParty : userParties) {
            Member member = userParty.getMember();
            if (member.getPoint() < requiredPoints) {
                return false; // 한 명이라도 포인트가 부족하면 false 반환
            }
        }
        deductPointsForMembers(userParties, requiredPoints); // 멤버의 포인트 차감
        return true;
    }

    @Transactional
    public void deductPointsForMembers(List<UserParty> userParties, Integer requiredPoints) {
        for (UserParty userParty : userParties) {
            Member member = userParty.getMember();
            member.minusPoint(requiredPoints);
            log.info("각 멤버들의 포인트 차감");
        }
    }

    public void createPayment(Party party) {
        Payment payment = Payment.builder()
                .party(party)
                .amount(party.getTargetPoint())
                .build();
        paymentRepository.save(payment);
    }

    public void createUserPayment(Party party) {
        List<UserParty> userParties = userPartyRepository.findUserPartiesByParty(party);

        for (UserParty userParty : userParties) {
            UserPayment userPayment = UserPayment.builder()
                    .member(userParty.getMember())
                    .party(party)
                    .amount(party.getPointPerPerson())
                    .build();
            userPaymentRepository.save(userPayment);
        }
    }

    public boolean isPartyFull(Party party) {
        Integer count = userPartyRepository.countUserPartiesByParty(party);
        return Objects.equals(count, party.getSize());
    }

    public List<PartyResponse> getHomeParties(Member loginMember) {
        validMember(loginMember);
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

    public List<MemberResponse> toMemberResponse(Party party) {
        List<UserParty> members = userPartyRepository.findUserPartiesByParty(party);
        return members.stream()
                .map(userParty -> MemberResponse.from(userParty.getMember()))
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> toPaymentResponse(Party party) {
        List<Payment> payments = paymentRepository.findAllByPartyId(party.getId());
        return payments.stream()
                .map(payment -> PaymentResponse.from(payment))
                .collect(Collectors.toList());
    }


}
