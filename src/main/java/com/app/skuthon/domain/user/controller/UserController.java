package com.app.skuthon.domain.user.controller;

import com.app.skuthon.domain.user.dto.request.SignUpRequest;
import com.app.skuthon.domain.user.dto.response.SignUpResponse;
import com.app.skuthon.domain.user.dto.response.UserPointResponse;
import com.app.skuthon.domain.user.entity.User;
import com.app.skuthon.domain.user.service.UserService;
import com.app.skuthon.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 API - 어떤 값 입력해도 무조건 1번 사용자 반환")
    public ResponseEntity<BaseResponse<SignUpResponse>> login(@RequestBody SignUpRequest request) {
        User user = userService.login(request.getLoginId(), request.getPassword());

        SignUpResponse response = new SignUpResponse(user.getId(), user.getNickname(), user.getPoints());
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<UserPointResponse>> getUser(@PathVariable Long userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(BaseResponse.success(
            new UserPointResponse(user.getId(), user.getNickname(), user.getPoints())));
    }
}