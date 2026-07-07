package com.app.skuthon.domain.mission.repository;

import com.app.skuthon.domain.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByGroupIdAndUserId(Long groupId, Long userId);

    List<Mission> findByGroupIdAndUserIdNot(Long groupId, Long userId);
}