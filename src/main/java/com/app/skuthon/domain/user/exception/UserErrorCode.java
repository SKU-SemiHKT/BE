package com.app.skuthon.domain.user.exception;

import com.app.skuthon.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    // --- 로그인 (Login) 오류 ---
    USER_NOT_FOUND("U001", "존재하지 않는 아이디입니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("U002", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatus status;
}