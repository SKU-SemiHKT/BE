package com.app.skuthon.domain.user.service;

import com.app.skuthon.domain.user.entity.User;
import com.app.skuthon.domain.user.exception.CustomException;
import com.app.skuthon.domain.user.exception.UserErrorCode;
import com.app.skuthon.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(String loginId, String rawPassword, String nickname) {
        // 아이디 중복 체크 로직 추가 (선택 사항)
        if (userRepository.findByLoginId(loginId).isPresent()) {
            throw new CustomException(UserErrorCode.DUPLICATE_LOGIN_ID); // 409 에러 사용
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(loginId, encodedPassword, nickname, 300);
        userRepository.save(user);
    }

    public User login(String loginId, String rawPassword) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND)); // 404 에러 사용

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_PASSWORD); // 401 에러 사용
        }

        return user;
    }
}