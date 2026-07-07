package com.app.skuthon.domain.settlement.dto.response;

import com.app.skuthon.domain.mission.entity.MissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title = "SettlementResultResponse DTO", description = "유저 1명 정산 결과")
public class SettlementResultResponse {

  @Schema(description = "정산 대상 유저", example = "3")
  private Long userId;

  @Schema(description = "닉네임", example = "juhee")
  private String nickname;

  @Schema(description = "판정 결과", example = "SUCCESS")
  private MissionStatus result;

  @Schema(description = "결과 반영된 미션 수", example = "2")
  private int settledMissions;

  @Schema(description = "정산된 베팅 수", example = "3")
  private int settledBets;

  @Schema(description = "적중한 베터 수", example = "2")
  private int winners;

  @Schema(description = "지급된 총 포인트", example = "250")
  private long totalPayout;
}