package com.app.skuthon.domain.bet.service;

import com.app.skuthon.domain.bet.dto.request.BetCreateRequest;
import com.app.skuthon.domain.bet.dto.response.BetResponse;
import com.app.skuthon.domain.bet.dto.response.MyBetHistoryResponse;
import com.app.skuthon.domain.bet.entity.Bet;
import com.app.skuthon.domain.bet.entity.BetStatus;
import com.app.skuthon.domain.bet.entity.BetType;
import com.app.skuthon.domain.bet.exception.BetErrorCode;
import com.app.skuthon.domain.bet.repository.BetRepository;
import com.app.skuthon.domain.mission.entity.Mission;
import com.app.skuthon.domain.mission.entity.MissionStatus;
import com.app.skuthon.domain.mission.repository.MissionRepository;
import com.app.skuthon.domain.point.entity.ReasonType;
import com.app.skuthon.domain.point.service.PointService;
import com.app.skuthon.domain.user.entity.User;
import com.app.skuthon.domain.user.repository.UserRepository;
import com.app.skuthon.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetService {

  private final BetRepository betRepository;
  private final UserRepository userRepository;
  private final PointService pointService;
  private final MissionRepository missionRepository;

  /** 베팅 걸기 */
  @Transactional
  public BetResponse placeBet(Long groupId, Long targetUserId, BetCreateRequest request) {
    LocalDate today = LocalDate.now();
    Long bettorId = request.getBettorId();

    // 1. 본인에게 베팅 금지
    if (targetUserId.equals(bettorId)) {
      throw new CustomException(BetErrorCode.SELF_BET_NOT_ALLOWED);
    }
    // 2. 금액 범위 (10~100)
    if (request.getBetAmount() < 10 || request.getBetAmount() > 100) {
      throw new CustomException(BetErrorCode.INVALID_BET_AMOUNT);
    }
    // 3. 중복 베팅
    if (betRepository.existsByGroupIdAndTargetUserIdAndBetDateAndBettorId(
        groupId, targetUserId, today, bettorId)) {
      throw new CustomException(BetErrorCode.DUPLICATE_BET);
    }
    // 4. 포인트 차감 (잔액 부족 시 예외)
    pointService.subtractPoints(bettorId, request.getBetAmount(), ReasonType.BET_PLACE, null);

    Bet bet = Bet.builder()
        .groupId(groupId)
        .targetUserId(targetUserId)
        .betDate(today)
        .bettorId(bettorId)
        .betAmount(request.getBetAmount())
        .betType(request.getBetType())
        .build();
    betRepository.save(bet);

    int remaining = userRepository.findById(bettorId).map(User::getPoints).orElse(0);
    return BetResponse.of(bet, remaining);
  }

  /** 베팅 취소 (예측 취소) */
  @Transactional
  public BetResponse cancelBet(Long betId, Long bettorId) {
    Bet bet = getMyPendingBet(betId, bettorId);

    pointService.addPoints(bettorId, bet.getBetAmount(), ReasonType.BET_CANCEL, bet.getId());
    betRepository.delete(bet);

    int remaining = userRepository.findById(bettorId).map(User::getPoints).orElse(0);
    return BetResponse.of(bet, remaining);
  }

  /** 베팅 변경 (결과 재선택) */
  @Transactional
  public BetResponse changeBet(Long betId, BetCreateRequest request) {
    Long bettorId = request.getBettorId();
    Bet bet = getMyPendingBet(betId, bettorId);

    if (request.getBetAmount() < 10 || request.getBetAmount() > 100) {
      throw new CustomException(BetErrorCode.INVALID_BET_AMOUNT);
    }

    // 기존 금액 환불 → 새 금액 차감 → 내용 변경
    pointService.addPoints(bettorId, bet.getBetAmount(), ReasonType.BET_CANCEL, bet.getId());
    pointService.subtractPoints(bettorId, request.getBetAmount(), ReasonType.BET_PLACE, bet.getId());
    bet.change(request.getBetType(), request.getBetAmount());

    int remaining = userRepository.findById(bettorId).map(User::getPoints).orElse(0);
    return BetResponse.of(bet, remaining);
  }

  private Bet getMyPendingBet(Long betId, Long bettorId) {
    Bet bet = betRepository.findById(betId)
        .orElseThrow(() -> new CustomException(BetErrorCode.BET_NOT_FOUND));
    if (!bet.getBettorId().equals(bettorId)) {
      throw new CustomException(BetErrorCode.NOT_YOUR_BET);
    }
    if (bet.getStatus() != BetStatus.PENDING) {
      throw new CustomException(BetErrorCode.ALREADY_SETTLED);
    }
    return bet;
  }

  /** 내 베팅 내역 (최신순) */
  @Transactional(readOnly = true)
  public List<MyBetHistoryResponse> getMyBetHistory(Long bettorId) {
    return betRepository.findByBettorIdOrderByCreatedAtDesc(bettorId).stream()
        .map(this::toHistoryCard)
        .toList();
  }

  private MyBetHistoryResponse toHistoryCard(Bet bet) {
    // 대상 유저 닉네임
    String nickname = userRepository.findById(bet.getTargetUserId())
        .map(User::getNickname).orElse("unknown");

    // 대상의 미션들 → 개수 + 결과 요약
    List<Mission> missions = missionRepository
        .findByGroupIdAndUserId(bet.getGroupId(), bet.getTargetUserId());
    MissionStatus missionResult = missions.stream()
        .map(Mission::getStatus)
        .filter(s -> s != MissionStatus.PENDING)
        .findFirst()
        .orElse(MissionStatus.PENDING);   // 하나라도 확정이면 그 결과 (일괄 판정이라 전부 동일)

    // 풀/배당률 (그 베팅판 기준)
    long successPool = betRepository.sumPool(bet.getGroupId(), bet.getTargetUserId(),
        bet.getBetDate(), BetType.SUCCESS);
    long failPool = betRepository.sumPool(bet.getGroupId(), bet.getTargetUserId(),
        bet.getBetDate(), BetType.FAIL);
    long total = successPool + failPool;                                    // ← 이 줄
    int successRate = (total == 0) ? 50 : (int) (successPool * 100 / total);  // ← 이 줄
    // 수익 계산
    Long profit = switch (bet.getStatus()) {
      case WIN -> {
        long winningPool = (bet.getBetType() == BetType.SUCCESS) ? successPool : failPool;
        long losingPool  = (bet.getBetType() == BetType.SUCCESS) ? failPool : successPool;
        long dividend = (winningPool == 0) ? 0 : losingPool * bet.getBetAmount() / winningPool;
        yield bet.getBetAmount() + dividend;
      }
      case LOSE -> (long) -bet.getBetAmount();
      case PENDING -> null;
    };

    return MyBetHistoryResponse.builder()
        .betId(bet.getId())
        .targetNickname(nickname)
        .missionCount(missions.size())
        .missionResult(missionResult)
        .myBetType(bet.getBetType())
        .myBetAmount(bet.getBetAmount())
        .betStatus(bet.getStatus())
        .profit(profit)
        .successPool(successPool)
        .failPool(failPool)
        .successRate(successRate)
        .failRate(100 - successRate)
        .build();
  }
}