package com.app.skuthon.domain.proof.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(title = "ProofFeedResponse DTO", description = "그룹 인증 피드 카드 (미인증자는 photoUrl null)")
public class ProofFeedResponse {

  @Schema(description = "유저 식별자", example = "3")
  private Long userId;

  @Schema(description = "닉네임", example = "주희")
  private String nickname;

  @Schema(description = "인증 사진 (미인증 시 null)", example = "/uploads/a1b2c3.jpg")
  private String photoUrl;

  @Schema(description = "인증 시각 (미인증 시 null)", example = "2026-07-07T21:24:00")
  private LocalDateTime watermarkTime;
}