package com.app.skuthon.domain.proof.exception;

import com.app.skuthon.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProofErrorCode implements BaseErrorCode {

  ALREADY_PROVED("P001", "오늘 이미 인증했습니다.", HttpStatus.CONFLICT),
  FILE_UPLOAD_FAILED("P002", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  EMPTY_FILE("P003", "파일이 비어있습니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}