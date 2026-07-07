package com.app.skuthon.domain.group.service;

import com.app.skuthon.domain.group.dto.response.GroupResponseDto;
import com.app.skuthon.domain.group.entity.GroupMember;
import com.app.skuthon.domain.group.entity.TeamGroup;
import com.app.skuthon.domain.group.repository.GroupMemberRepository;
import com.app.skuthon.domain.group.repository.TeamGroupRepository;
import com.app.skuthon.domain.user.entity.User;
import com.app.skuthon.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final TeamGroupRepository teamGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    // 1. 그룹 생성
    @Transactional
    public GroupResponseDto.Create createGroup(Long userId, String name, String mainGoal, LocalDate deadline) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 6자리 랜덤 초대 코드 생성 (대문자+숫자)
        String inviteCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        TeamGroup newGroup = new TeamGroup(name, mainGoal, deadline, inviteCode);
        teamGroupRepository.save(newGroup);

        GroupMember groupMember = new GroupMember(user, newGroup);
        groupMemberRepository.save(groupMember);

        return new GroupResponseDto.Create(newGroup.getId(), newGroup.getName(), newGroup.getMainGoal(), newGroup.getDeadline(), newGroup.getInviteCode());
    }

    // 2. 그룹 참여
    @Transactional
    public GroupResponseDto.Join joinGroup(Long userId, String inviteCode) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        TeamGroup teamGroup = teamGroupRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 초대 코드입니다."));

        if (groupMemberRepository.existsByUserAndTeamGroup(user, teamGroup)) {
            throw new IllegalArgumentException("이미 가입된 그룹입니다.");
        }

        groupMemberRepository.save(new GroupMember(user, teamGroup));

        return new GroupResponseDto.Join(teamGroup.getId(), teamGroup.getName());
    }

    // 3. 내 그룹 목록
    @Transactional(readOnly = true)
    public List<GroupResponseDto.ListInfo> getMyGroups(Long userId) {
        List<GroupMember> myGroups = groupMemberRepository.findByUserId(userId);

        return myGroups.stream().map(member -> {
            TeamGroup group = member.getTeamGroup();
            int memberCount = groupMemberRepository.countByTeamGroup(group);
            long dDay = ChronoUnit.DAYS.between(LocalDate.now(), group.getDeadline());

            return new GroupResponseDto.ListInfo(group.getId(), group.getName(), group.getMainGoal(), group.getDeadline(), memberCount, dDay);
        }).collect(Collectors.toList());
    }

    // 4. 그룹 상세
    @Transactional(readOnly = true)
    public GroupResponseDto.Detail getGroupDetail(Long groupId) {
        TeamGroup group = teamGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹 없음"));

        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), group.getDeadline());

        List<GroupMember> members = groupMemberRepository.findByTeamGroup(group);
        List<GroupResponseDto.MemberInfo> memberInfos = members.stream()
                .map(m -> new GroupResponseDto.MemberInfo(m.getUser().getId(), m.getUser().getNickname()))
                .collect(Collectors.toList());

        return new GroupResponseDto.Detail(group.getId(), group.getName(), group.getMainGoal(), group.getDeadline(), dDay, memberInfos);
    }
}