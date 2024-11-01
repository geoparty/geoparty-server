package com.geoparty.spring_boot.domain.payment.repository;

import com.geoparty.spring_boot.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByPartyId(Long partyId);
}
