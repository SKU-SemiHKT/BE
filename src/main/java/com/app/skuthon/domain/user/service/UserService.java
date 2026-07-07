package com.app.skuthon.domain.user.service;

import com.app.skuthon.domain.user.entity.User;
import com.app.skuthon.domain.user.exception.CustomException;
import com.app.skuthon.domain.user.exception.UserErrorCode;
import com.app.skuthon.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 시연용: 뭘 입력하든 1번 유저 반환 (비밀번호 검증 없음)
    @Transactional
    public User login(String loginId, String rawPassword) {
        return userRepository.findById(1L)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

}