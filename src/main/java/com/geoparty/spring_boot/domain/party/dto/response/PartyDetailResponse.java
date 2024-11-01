package com.geoparty.spring_boot.domain.party.dto.response;

import com.geoparty.spring_boot.domain.member.dto.MemberDto;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.payment.dto.response.PaymentResponse;
import com.geoparty.spring_boot.domain.payment.entity.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PartyDetailResponse {

    private PartyResponse partyResponse;
    private List<MemberDto> members;
    private List<PaymentResponse> payments;

    @Builder
    public PartyDetailResponse(PartyResponse partyResponse, List<MemberDto> members, List<PaymentResponse> payments) {
        this.partyResponse = partyResponse;
        this.members = members;
        this.payments = payments;
    }

    public static PartyDetailResponse from(PartyResponse partyResponse, List<MemberDto> members, List<PaymentResponse> payments) {
        return PartyDetailResponse.builder()
                .partyResponse(partyResponse)
                .members(members)
                .payments(payments)
                .build();
    }
}
