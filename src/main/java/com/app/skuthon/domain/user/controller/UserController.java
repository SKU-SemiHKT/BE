package com.app.skuthon.domain.user.controller;

import com.app.skuthon.domain.user.dto.request.SignUpRequest;
import com.app.skuthon.domain.user.dto.response.SignUpResponse;
import com.app.skuthon.domain.user.entity.User;
import com.app.skuthon.domain.user.service.UserService;
import com.app.skuthon.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import ... (DTO, UserService, User 등)

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<SignUpResponse>> signup(@RequestBody SignUpRequest request) {
        // UserService의 signup 메서드를 호출 (loginId, password, nickname 전달)
        userService.signup(request.getLoginId(), request.getPassword(), request.getNickname());

        // 방금 가입한 유저 정보를 다시 조회해서 응답으로 내려줌 (또는 Service에서 반환하게 수정해도 됨)
        User user = userService.login(request.getLoginId(), request.getPassword());

        SignUpResponse response = new SignUpResponse(user.getId(), user.getNickname(), user.getPoints());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse.success(201, "회원가입이 완료되었습니다.", response));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<SignUpResponse>> login(@RequestBody SignUpRequest request) {
        User user = userService.login(request.getLoginId(), request.getPassword());

        SignUpResponse response = new SignUpResponse(user.getId(), user.getNickname(), user.getPoints());
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}