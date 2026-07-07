package com.app.skuthon.domain.group.repository;

import com.app.skuthon.domain.group.entity.GroupMember;
import com.app.skuthon.domain.group.entity.TeamGroup;
import com.app.skuthon.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    // 1. 특정 유저가 속한 그룹 멤버 목록 조회 (내 그룹 목록용)
    List<GroupMember> findByUserId(Long userId);

    // 2. 이미 가입된 그룹인지 확인 (중복 참여 방지용)
    boolean existsByUserAndTeamGroup(User user, TeamGroup teamGroup);

    // 3. 특정 그룹의 총 멤버 수 카운트 (목록 조회 시 인원수 표시용)
    int countByTeamGroup(TeamGroup teamGroup);

    // 4. 특정 그룹에 속한 멤버 전체 조회 (그룹 상세 조회 시 멤버 리스트용)
    List<GroupMember> findByTeamGroup(TeamGroup teamGroup);

}