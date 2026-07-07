package com.app.skuthon.domain.point.service;

import com.app.skuthon.domain.point.entity.PointHistory;
import com.app.skuthon.domain.point.entity.ReasonType;
import com.app.skuthon.domain.point.repository.PointHistoryRepository;
import com.app.skuthon.domain.user.entity.User;
import com.app.skuthon.domain.user.exception.UserErrorCode;
import com.app.skuthon.domain.user.repository.UserRepository;
import com.app.skuthon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PointService {

  private final UserRepository userRepository;
  private final PointHistoryRepository pointHistoryRepository;

  @Transactional
  public void addPoints(Long userId, int amount, ReasonType reasonType, Long relatedId) {
    User user = getUser(userId);
    user.addPoints(amount);
    pointHistoryRepository.save(PointHistory.of(userId, amount, reasonType, relatedId));
  }

  @Transactional
  public void subtractPoints(Long userId, int amount, ReasonType reasonType, Long relatedId) {
    User user = getUser(userId);
    user.subtractPoints(amount);   // 잔액 부족 시 예외
    pointHistoryRepository.save(PointHistory.of(userId, -amount, reasonType, relatedId));
  }

  private User getUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
  }
}
