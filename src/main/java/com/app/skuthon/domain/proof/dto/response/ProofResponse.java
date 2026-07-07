package com.app.skuthon.domain.proof.dto.response;

import com.app.skuthon.domain.proof.entity.DailyProof;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(title = "ProofResponse DTO", description = "인증샷 업로드 응답")
public class ProofResponse {

  @Schema(description = "인증샷 식별자", example = "7")
  private Long proofId;

  @Schema(description = "인증 사진 경로", example = "/uploads/a1b2c3.jpg")
  private String photoUrl;

  @Schema(description = "인증 시각 (워터마크용)", example = "2026-07-07T21:24:00")
  private LocalDateTime watermarkTime;

  @Schema(description = "인증 보상 지급 후 잔여 포인트", example = "330")
  private Integer remainingPoints;

  public static ProofResponse of(DailyProof proof, Integer remainingPoints) {
    return new ProofResponse(proof.getId(), proof.getPhotoUrl(),
        proof.getWatermarkTime(), remainingPoints);
  }
}