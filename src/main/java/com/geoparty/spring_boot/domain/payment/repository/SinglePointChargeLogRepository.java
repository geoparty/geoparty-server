package com.geoparty.spring_boot.domain.payment.repository;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.payment.entity.SinglePointChargeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SinglePointChargeLogRepository extends JpaRepository<SinglePointChargeLog, Long> {
    Optional<SinglePointChargeLog> findByTid(String tid);

}
