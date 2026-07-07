package com.app.skuthon.domain.proof.entity;

import com.app.skuthon.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "daily_proof",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_proof_user_group_date",
        columnNames = {"user_id", "group_id", "proof_date"}))
public class DailyProof extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "group_id", nullable = false)
  private Long groupId;

  @Column(name = "proof_date", nullable = false)
  private LocalDate proofDate;

  @Column(name = "photo_url", nullable = false)
  private String photoUrl;

  @Column(name = "watermark_time")
  private LocalDateTime watermarkTime;
}