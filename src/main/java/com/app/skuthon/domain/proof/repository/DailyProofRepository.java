package com.app.skuthon.domain.proof.repository;

import com.app.skuthon.domain.proof.entity.DailyProof;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyProofRepository extends JpaRepository<DailyProof, Long> {

  boolean existsByUserIdAndGroupIdAndProofDate(Long userId, Long groupId, LocalDate proofDate);

  Optional<DailyProof> findByUserIdAndGroupIdAndProofDate(Long userId, Long groupId, LocalDate proofDate);

  List<DailyProof> findByGroupIdAndProofDate(Long groupId, LocalDate proofDate);
}