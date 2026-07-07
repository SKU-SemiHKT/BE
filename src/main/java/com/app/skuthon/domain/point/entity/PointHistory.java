package com.app.skuthon.domain.point.entity;

import com.app.skuthon.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "point")
public class PointHistory extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false)
  private int amount;                 // 양수(+), 음수(-)

  @Enumerated(EnumType.STRING)
  @Column(name = "reason_type", nullable = false, length = 50)
  private ReasonType reasonType;

  @Column(name = "related_id")
  private Long relatedId;             // 베팅ID, 인증ID 등 추적용

  public static PointHistory of(Long userId, int amount, ReasonType reasonType, Long relatedId) {
    return PointHistory.builder()
        .userId(userId)
        .amount(amount)
        .reasonType(reasonType)
        .relatedId(relatedId)
        .build();
  }
}
