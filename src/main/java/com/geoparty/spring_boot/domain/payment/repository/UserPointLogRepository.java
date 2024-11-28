package com.geoparty.spring_boot.domain.payment.repository;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.payment.entity.SinglePointChargeLog;
import com.geoparty.spring_boot.domain.payment.entity.UserPointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserPointLogRepository extends JpaRepository<UserPointLog, Long> {
    @Query("SELECT up FROM UserPointLog up " +
            "WHERE up.member = :member")
    List<UserPointLog> findAllByMember(Member member);
}
