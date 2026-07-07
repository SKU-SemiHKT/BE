package com.app.skuthon.domain.mission.service;

import com.app.skuthon.domain.bet.entity.Bet;
import com.app.skuthon.domain.bet.entity.BetType;
import com.app.skuthon.domain.bet.repository.BetRepository;
import com.app.skuthon.domain.group.repository.GroupMemberRepository;
import com.app.skuthon.domain.mission.dto.request.MissionRequestDto;
import com.app.skuthon.domain.mission.dto.response.MissionResponseDto;
import com.app.skuthon.domain.mission.entity.Mission;
import com.app.skuthon.domain.mission.exception.MissionErrorCode;
import com.app.skuthon.domain.mission.repository.MissionRepository;
import com.app.skuthon.domain.user.entity.User;

import com.app.skuthon.domain.user.exception.UserErrorCode;
import com.app.skuthon.domain.user.repository.UserRepository;
import com.app.skuthon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final BetRepository betRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    // 3-1. 미션 등록
    @Transactional
    public MissionResponseDto.Create createMission(MissionRequestDto.Create request) {

        if (!groupMemberRepository.existsByUserIdAndTeamGroupId(request.getUserId(), request.getGroupId())) {
            throw new CustomException(MissionErrorCode.NOT_GROUP_MEMBER);
        }

        Mission mission = Mission.builder()
                .userId(request.getUserId())
                .groupId(request.getGroupId())
                .content(request.getContent())
                .build();

        missionRepository.save(mission);

        return new MissionResponseDto.Create(
                mission.getId(),
                mission.getContent(),
                mission.getStatus().name()
        );
    }

    // 3-2. 내 미션 목록 조회
    @Transactional(readOnly = true)
    public List<MissionResponseDto.MyList> getMyMissions(Long groupId, Long userId) {
        List<Mission> myMissions = missionRepository.findByGroupIdAndUserId(groupId, userId);
        return myMissions.stream()
                .map(m -> new MissionResponseDto.MyList(
                        m.getId(),
                        m.getContent(),
                        m.getStatus().name()))
                .collect(Collectors.toList());
    }

    // 3-3. 베팅판 목록 조회
    @Transactional(readOnly = true)
    public List<MissionResponseDto.BettingBoard> getBettingBoard(Long groupId, Long userId) {
        // 1. 당일 해당 그룹의 미션 중, 내 미션이 아닌 것들만 가져오기
        List<Mission> otherMissions = missionRepository.findByGroupIdAndUserIdNot(groupId, userId);

        return otherMissions.stream().map(mission -> {
            Long targetUserId = mission.getUserId();

            // 대상 유저 닉네임 조회
            User targetUser = userRepository.findById(targetUserId)
                    .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

            LocalDate today = LocalDate.now();

            // 베팅 풀 계산
            int successPool = (int) betRepository.sumPool(groupId, targetUserId, today, BetType.SUCCESS);
            int failPool = (int) betRepository.sumPool(groupId, targetUserId, today, BetType.FAIL);
            int totalPool = successPool + failPool;

            // 배당률 계산 (0이면 50:50)
            int successRate = totalPool == 0 ? 50 : (int) ((double) successPool / totalPool * 100);
            int failRate = totalPool == 0 ? 50 : 100 - successRate;

            // 베팅 인원수 계산
            int successBettors = (int) betRepository.countByGroupIdAndTargetUserIdAndBetDateAndBetType(groupId, targetUserId, today, BetType.SUCCESS);
            int failBettors = (int) betRepository.countByGroupIdAndTargetUserIdAndBetDateAndBetType(groupId, targetUserId, today, BetType.FAIL);

            // 내 베팅 정보 확인
            Optional<Bet> myBetOpt = betRepository.findByGroupIdAndTargetUserIdAndBetDateAndBettorId(groupId, targetUserId, today, userId);
            MissionResponseDto.MyBetInfo myBetInfo = myBetOpt.map(bet ->
                    new MissionResponseDto.MyBetInfo(bet.getId(), bet.getBetType().name(), bet.getBetAmount())
            ).orElse(null);

            return new MissionResponseDto.BettingBoard(
                    mission.getId(),
                    targetUserId,
                    targetUser.getNickname(),
                    mission.getContent(),
                    mission.getStatus().name(),
                    successPool,
                    failPool,
                    successRate,
                    failRate,
                    successBettors,
                    failBettors,
                    myBetInfo
            );
        }).collect(Collectors.toList());
    }
}