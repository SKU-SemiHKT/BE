package com.app.skuthon.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title = "사용자 포인트 내역 응답 DTO")
public class UserPointResponse {

  private Long userId;

  private String nickname;

  private Integer points;

}
