package com.geoparty.spring_boot.domain.party.service;

import com.geoparty.spring_boot.domain.party.dto.request.PartyRequest;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.repository.PartyRepository;
import com.geoparty.spring_boot.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {

    private final PartyRepository partyRepository;

    @Transactional
    public Party createParty(PartyRequest request, UserAccount user) {
        Party party = request.toEntity();
        partyRepository.save(party);

        return party;
    }
}
