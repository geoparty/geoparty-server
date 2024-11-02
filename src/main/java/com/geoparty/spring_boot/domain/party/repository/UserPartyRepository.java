package com.geoparty.spring_boot.domain.party.repository;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.party.entity.Party;
import com.geoparty.spring_boot.domain.party.entity.UserParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPartyRepository extends JpaRepository<UserParty, Long> {
    @Query("SELECT up FROM UserParty up " +
            "JOIN FETCH up.party p " +
            "JOIN FETCH p.organization " +
            "WHERE up.member = :member")
    List<UserParty> findUserPartiesByMember(@Param("member") Member member);

    @Query("SELECT up FROM UserParty up " +
            "JOIN FETCH up.party p " +
            "JOIN FETCH up.member m " +
            "WHERE p = :party")
    List<UserParty> findUserPartiesByParty(@Param("party") Party party);
}
