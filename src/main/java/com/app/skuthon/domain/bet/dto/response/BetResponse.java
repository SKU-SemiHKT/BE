package com.app.skuthon.domain.bet.dto.response;

import com.app.skuthon.domain.bet.entity.Bet;
import com.app.skuthon.domain.bet.entity.BetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title = "BetResponse DTO", description = "베팅 응답")
public class BetResponse {

  @Schema(description = "베팅 식별자", example = "55")
  private Long betId;

  @Schema(description = "베팅 타입", example = "SUCCESS")
  private BetType betType;

  @Schema(description = "베팅 금액", example = "100")
  private int betAmount;

  @Schema(description = "베팅 후 남은 포인트", example = "200")
  private Integer remainingPoints;

  public static BetResponse of(Bet bet, Integer remainingPoints) {
    return new BetResponse(bet.getId(), bet.getBetType(), bet.getBetAmount(), remainingPoints);
  }
}