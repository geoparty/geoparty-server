package com.geoparty.spring_boot.domain.party.repository;

import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Long> {
    List<Party> findByOrganization(Organization organizationId);
    @Query("SELECT p FROM Party p " +
                    "JOIN FETCH p.organization " +
                    "WHERE p.title LIKE %:title%")
    List<Party> findByTitle(@Param("title") String title);
    List<Party> findByPayDate(LocalDate payDate);
    Integer countByOrganizationId(Long organizationId);

    @Query("SELECT p FROM Party p " +
            "JOIN FETCH p.organization o " +
            "WHERE o = :organization AND p.title LIKE %:title%")
    List<Party> findByOrganizationAndTitle(@Param("organization") Organization organization,
                                           @Param("title") String title);
}