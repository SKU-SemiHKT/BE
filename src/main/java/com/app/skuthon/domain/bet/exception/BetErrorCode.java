package com.app.skuthon.domain.bet.exception;

import com.app.skuthon.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BetErrorCode implements BaseErrorCode {

  SELF_BET_NOT_ALLOWED("B001", "본인에게는 베팅할 수 없습니다.", HttpStatus.FORBIDDEN),
  INVALID_BET_AMOUNT("B002", "베팅은 10~100P 사이만 가능합니다.", HttpStatus.BAD_REQUEST),
  DUPLICATE_BET("B003", "이미 베팅했습니다.", HttpStatus.CONFLICT),
  BET_NOT_FOUND("B004", "베팅이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
  ALREADY_SETTLED("B005", "이미 정산된 베팅입니다.", HttpStatus.CONFLICT),
  NOT_YOUR_BET("B006", "본인의 베팅만 수정/취소할 수 있습니다.", HttpStatus.FORBIDDEN);

  private final String code;
  private final String message;
  private final HttpStatus status;
}