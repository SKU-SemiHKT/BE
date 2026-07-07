package com.app.skuthon.domain.mission.exception;

import com.app.skuthon.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MissionErrorCode implements BaseErrorCode {

    MISSION_NOT_FOUND("M001", "존재하지 않는 미션입니다.", HttpStatus.NOT_FOUND),
    NOT_GROUP_MEMBER("M002", "해당 그룹의 멤버가 아닙니다.", HttpStatus.FORBIDDEN),
    ALREADY_BET("M003", "이미 베팅한 미션입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}