package com.geoparty.spring_boot.domain.party.repository;

import com.geoparty.spring_boot.domain.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}