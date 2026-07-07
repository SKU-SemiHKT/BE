package com.app.skuthon.domain.bet.dto.request;

import com.app.skuthon.domain.bet.entity.BetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "BetCreateRequest DTO", description = "베팅 걸기/변경 요청")
public class BetCreateRequest {

  @Schema(description = "베팅하는 사용자 식별자", example = "1")
  private Long bettorId;

  @Schema(description = "베팅 타입 (성공/실패 예측)", example = "SUCCESS")
  private BetType betType;

  @Schema(description = "베팅 금액 (10~100)", example = "100")
  private int betAmount;
}