package com.geoparty.spring_boot.domain.payment.repository;

import com.geoparty.spring_boot.domain.payment.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {
    Optional<PointLog> findByTid(String tid);
}
