package com.app.skuthon.domain.point.repository;

import com.app.skuthon.domain.point.entity.PointHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
  List<PointHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}