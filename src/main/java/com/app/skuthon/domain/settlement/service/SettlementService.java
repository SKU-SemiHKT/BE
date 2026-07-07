// domain/settlement/service/SettlementService.java
package com.app.skuthon.domain.settlement.service;

import com.app.skuthon.domain.bet.entity.Bet;
import com.app.skuthon.domain.bet.entity.BetStatus;
import com.app.skuthon.domain.bet.entity.BetType;
import com.app.skuthon.domain.bet.repository.BetRepository;
import com.app.skuthon.domain.group.entity.GroupMember;
import com.app.skuthon.domain.group.entity.TeamGroup;
import com.app.skuthon.domain.group.repository.GroupMemberRepository;
import com.app.skuthon.domain.group.repository.TeamGroupRepository;
import com.app.skuthon.domain.mission.entity.Mission;
import com.app.skuthon.domain.mission.entity.MissionStatus;
import com.app.skuthon.domain.mission.repository.MissionRepository;
import com.app.skuthon.domain.point.entity.ReasonType;
import com.app.skuthon.domain.point.service.PointService;
import com.app.skuthon.domain.proof.repository.DailyProofRepository;
import com.app.skuthon.domain.settlement.dto.response.SettlementResultResponse;
import com.app.skuthon.domain.user.entity.User;
import com.app.skuthon.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementService {

  private final MissionRepository missionRepository;
  private final BetRepository betRepository;
  private final GroupMemberRepository groupMemberRepository;
  private final TeamGroupRepository teamGroupRepository;
  private final DailyProofRepository dailyProofRepository;
  private final UserRepository userRepository;
  private final PointService pointService;

  /**
   * 한 유저의 하루 정산 (공통 코어)
   * - 인증 업로드 직후: result = SUCCESS 로 호출
   * - 자정/수동 실패 처리: result = FAIL 로 호출
   */
  @Transactional
  public SettlementResultResponse settleUser(Long groupId, Long targetUserId, MissionStatus result) {
    LocalDate today = LocalDate.now();

    // 1. PENDING 미션 전부 결과 반영
    List<Mission> missions = missionRepository.findByGroupIdAndUserId(groupId, targetUserId).stream()
        .filter(m -> m.getStatus() == MissionStatus.PENDING)
        .toList();
    missions.forEach(m -> m.updateStatus(result));

    // 2. 이 유저에게 걸린 오늘의 PENDING 베팅 정산
    List<Bet> bets = betRepository.findByGroupIdAndTargetUserIdAndBetDateAndStatus(
        groupId, targetUserId, today, BetStatus.PENDING);

    BetType winType = (result == MissionStatus.SUCCESS) ? BetType.SUCCESS : BetType.FAIL;

    long winningPool = bets.stream()
        .filter(b -> b.getBetType() == winType)
        .mapToLong(Bet::getBetAmount).sum();
    long losingPool = bets.stream()
        .filter(b -> b.getBetType() != winType)
        .mapToLong(Bet::getBetAmount).sum();

    int winners = 0;
    long totalPayout = 0;

    for (Bet bet : bets) {
      if (bet.getBetType() == winType && winningPool > 0) {
        // 배당 = 원금 + (진쪽풀 × 내베팅 ÷ 이긴쪽풀), 정수 나눗셈(버림)
        long dividend = losingPool * bet.getBetAmount() / winningPool;
        long payout = bet.getBetAmount() + dividend;

        pointService.addPoints(bet.getBettorId(), (int) payout, ReasonType.BET_WIN, bet.getId());
        bet.win();
        winners++;
        totalPayout += payout;
      } else {
        bet.lose();   // 이긴 쪽 풀이 0인 경우 포함: 전원 LOSE
      }
    }

    String nickname = userRepository.findById(targetUserId)
        .map(User::getNickname).orElse("unknown");

    return new SettlementResultResponse(
        targetUserId, nickname, result, missions.size(), bets.size(), winners, totalPayout);
  }

  /**
   * 미인증자 일괄 실패 정산 (자정 스케줄러 / 수동 트리거 공용)
   */
  @Transactional
  public List<SettlementResultResponse> settleFailures(Long groupId) {
    LocalDate today = LocalDate.now();

    TeamGroup group = teamGroupRepository.findById(groupId)
        .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

    List<SettlementResultResponse> results = new ArrayList<>();

    for (GroupMember member : groupMemberRepository.findByTeamGroup(group)) {
      Long userId = member.getUser().getId();

      // 오늘 인증한 사람은 이미 업로드 시점에 SUCCESS 정산됨 → 스킵
      if (dailyProofRepository.existsByUserIdAndGroupIdAndProofDate(userId, groupId, today)) {
        continue;
      }
      results.add(settleUser(groupId, userId, MissionStatus.FAIL));
    }
    return results;
  }
}