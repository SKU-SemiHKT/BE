package com.app.skuthon.domain.group.exception;

import com.app.skuthon.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GroupErrorCode implements BaseErrorCode {

    // 순서: code, message, status
    GROUP_NOT_FOUND("G001", "존재하지 않는 그룹입니다.", HttpStatus.NOT_FOUND),
    INVALID_INVITE_CODE("G002", "유효하지 않은 초대 코드입니다.", HttpStatus.NOT_FOUND),
    ALREADY_JOINED_GROUP("G003", "이미 가입된 그룹입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}