package com.geoparty.spring_boot.domain.payment.repository;

import com.geoparty.spring_boot.domain.payment.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
