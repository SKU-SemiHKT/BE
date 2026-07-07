package com.app.skuthon.domain.group.repository;

import com.app.skuthon.domain.group.entity.TeamGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamGroupRepository extends JpaRepository<TeamGroup, Long> {

    // 초대 코드로 그룹 찾기
    Optional<TeamGroup> findByInviteCode(String inviteCode);

}