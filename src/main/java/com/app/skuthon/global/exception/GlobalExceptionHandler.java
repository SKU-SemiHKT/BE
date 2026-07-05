package com.app.skuthon.global.exception;

import com.app.skuthon.global.common.BaseResponse;
import com.app.skuthon.global.exception.model.BaseErrorCode;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice   /// Controller에서 발생하는 예외를 처리해서 반환해주는 역할
public class GlobalExceptionHandler {

  // 커스텀 예외
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<BaseResponse<Object>> handleCustomException(CustomException ex) {
    BaseErrorCode errorCode = ex.getErrorCode();
    log.warn("CustomException 발생: {} - {}", errorCode.getCode(), errorCode.getMessage());
    return ResponseEntity.status(errorCode.getStatus())
        .body(BaseResponse.error(errorCode.getCode(), errorCode.getMessage()));
  }

  // Validation 실패
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<?>> handleValidationException(
      MethodArgumentNotValidException ex) {
    String errorMessages =
        ex.getBindingResult().getFieldErrors().stream()
            .map(e -> String.format("[%s] %s", e.getField(), e.getDefaultMessage()))
            .collect(Collectors.joining(" / "));
    log.warn("Validation 오류 발생: {}", errorMessages);
    return ResponseEntity.badRequest().body(BaseResponse.error(GlobalErrorCode.INVALID_INPUT_VALUE.getCode(), GlobalErrorCode.INVALID_INPUT_VALUE.getMessage()));
  }

  // 예상치 못한 예외
  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<?>> handleException(Exception ex) {
    log.error("Server 오류 발생: ", ex);
    return ResponseEntity.status(GlobalErrorCode.INTERNAL_SERVER_ERROR.getStatus())
        .body(BaseResponse.error(GlobalErrorCode.INTERNAL_SERVER_ERROR.getCode(), GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
  }

  //잘못된 요청
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<BaseResponse<?>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    log.warn("HttpMessageNotReadableException 발생: {}", ex.getMessage());
    return ResponseEntity.badRequest()
        .body(BaseResponse.error(GlobalErrorCode.INVALID_INPUT_VALUE.getCode(), GlobalErrorCode.INVALID_INPUT_VALUE.getMessage()));
  }

  //권한 없는 사용자 요청
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<BaseResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
    log.warn("AccessDeniedException 발생: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(BaseResponse.error(GlobalErrorCode.ACCESS_DENIED.getCode(), GlobalErrorCode.ACCESS_DENIED.getMessage()));
  }
}
