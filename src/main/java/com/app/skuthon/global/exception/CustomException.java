package com.app.skuthon.global.exception;

import com.app.skuthon.domain.user.exception.UserErrorCode;
import com.app.skuthon.global.exception.model.BaseErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final BaseErrorCode errorCode;

  public CustomException(UserErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
