package com.app.skuthon.domain.user.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private String loginId;
    private String password;
    private String nickname; // 로그인할 땐 안 쓰이지만 회원가입용으로 포함
}