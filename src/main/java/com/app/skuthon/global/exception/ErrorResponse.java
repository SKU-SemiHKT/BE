package com.app.skuthon.global.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "에러 응답")
public record ErrorResponse(
    @Schema(description = "에러 메시지", example = "계약을 찾을 수 없습니다.") String message,
    @Schema(description = "HTTP 상태 코드", example = "404") int status
) {}
