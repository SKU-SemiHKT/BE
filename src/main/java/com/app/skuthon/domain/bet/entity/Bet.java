package com.app.skuthon.domain.bet.entity;

import com.app.skuthon.domain.point.entity.ReasonType;
import com.app.skuthon.domain.point.service.PointService;
import com.app.skuthon.domain.user.entity.User;
import com.app.skuthon.domain.user.exception.UserErrorCode;
import com.app.skuthon.domain.user.repository.UserRepository;
import com.app.skuthon.global.common.BaseResponse;
import com.app.skuthon.global.common.BaseTimeEntity;
import com.app.skuthon.global.exception.model.BaseErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bet",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_bet_target_date_bettor",
        columnNames = {"group_id", "target_user_id", "bet_date", "bettor_id"}))
public class Bet extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "group_id", nullable = false)
  private Long groupId;

  @Column(name = "target_user_id", nullable = false)
  private Long targetUserId;

  @Column(name = "bet_date", nullable = false)
  private LocalDate betDate;

  @Column(name = "bettor_id", nullable = false)
  private Long bettorId;

  @Column(name = "bet_amount", nullable = false)
  private int betAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "bet_type", nullable = false)
  private BetType betType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private BetStatus status = BetStatus.PENDING;

  public void win()  { this.status = BetStatus.WIN; }
  public void lose() { this.status = BetStatus.LOSE; }

  public void change(BetType newType, int newAmount) {
    this.betType = newType;
    this.betAmount = newAmount;
  }
}
