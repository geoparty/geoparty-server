package com.geoparty.spring_boot.domain.party.repository;

import com.geoparty.spring_boot.domain.party.entity.UserParty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPartyRepository extends JpaRepository<UserParty, Long> {
}
