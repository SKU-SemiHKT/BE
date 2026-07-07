package com.app.skuthon.domain.bet.dto.response;

import com.app.skuthon.domain.bet.entity.BetStatus;
import com.app.skuthon.domain.bet.entity.BetType;
import com.app.skuthon.domain.mission.entity.MissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "MyBetHistoryResponse DTO", description = "내 베팅 내역 카드")
public class MyBetHistoryResponse {

  @Schema(description = "베팅 식별자", example = "55")
  private Long betId;

  @Schema(description = "베팅 대상 닉네임", example = "juhee")
  private String targetNickname;

  @Schema(description = "대상의 미션 개수 ('미션1,2' 표기용)", example = "2")
  private int missionCount;

  @Schema(description = "미션 결과 (PENDING이면 진행중)", example = "SUCCESS")
  private MissionStatus missionResult;

  @Schema(description = "내 예측", example = "SUCCESS")
  private BetType myBetType;

  @Schema(description = "내 베팅 금액", example = "100")
  private int myBetAmount;

  @Schema(description = "베팅 상태", example = "WIN")
  private BetStatus betStatus;

  @Schema(description = "포인트 수익 (WIN: 원금+배당 / LOSE: -베팅액 / PENDING: null)", example = "150")
  private Long profit;

  @Schema(description = "성공 풀", example = "100")
  private long successPool;

  @Schema(description = "실패 풀", example = "50")
  private long failPool;

  @Schema(description = "성공 비율(%)", example = "67")
  private int successRate;

  @Schema(description = "실패 비율(%)", example = "33")
  private int failRate;
}