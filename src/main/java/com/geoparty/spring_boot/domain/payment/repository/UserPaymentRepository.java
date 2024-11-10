package com.geoparty.spring_boot.domain.payment.repository;

import com.geoparty.spring_boot.domain.payment.entity.Payment;
import com.geoparty.spring_boot.domain.payment.entity.UserPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPaymentRepository extends JpaRepository<UserPayment, Long> {
    List<UserPayment> findAllByMemberId(Long memberId);
}