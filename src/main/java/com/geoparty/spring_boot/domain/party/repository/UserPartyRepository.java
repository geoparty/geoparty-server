package com.geoparty.spring_boot.domain.party.repository;

import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.entity.UserParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPartyRepository extends JpaRepository<UserParty, Long> {
    @Query("SELECT up.party FROM UserParty up "
            + "JOIN FETCH up.party "
            + "WHERE up.member.userId = :userId")
    List<Party> findPartiesByUserId(@Param("userId") Integer userId);
}
