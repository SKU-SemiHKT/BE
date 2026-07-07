package com.app.skuthon.domain.bet.repository;

import com.app.skuthon.domain.bet.entity.Bet;
import com.app.skuthon.domain.bet.entity.BetStatus;
import com.app.skuthon.domain.bet.entity.BetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BetRepository extends JpaRepository<Bet, Long> {

  boolean existsByGroupIdAndTargetUserIdAndBetDateAndBettorId(
      Long groupId, Long targetUserId, LocalDate betDate, Long bettorId);

  Optional<Bet> findByGroupIdAndTargetUserIdAndBetDateAndBettorId(
      Long groupId, Long targetUserId, LocalDate betDate, Long bettorId);

  List<Bet> findByGroupIdAndTargetUserIdAndBetDateAndStatus(
      Long groupId, Long targetUserId, LocalDate betDate, BetStatus status);

  List<Bet> findByBettorIdOrderByCreatedAtDesc(Long bettorId);

  @Query("select coalesce(sum(b.betAmount), 0) from Bet b " +
      "where b.groupId = :groupId and b.targetUserId = :targetUserId " +
      "and b.betDate = :betDate and b.betType = :type")
  long sumPool(@Param("groupId") Long groupId,
      @Param("targetUserId") Long targetUserId,
      @Param("betDate") LocalDate betDate,
      @Param("type") BetType type);

  long countByGroupIdAndTargetUserIdAndBetDateAndBetType(
      Long groupId, Long targetUserId, LocalDate betDate, BetType type);
}
