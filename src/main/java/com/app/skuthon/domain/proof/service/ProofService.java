package com.app.skuthon.domain.proof.service;

import com.app.skuthon.domain.group.entity.GroupMember;
import com.app.skuthon.domain.group.entity.TeamGroup;
import com.app.skuthon.domain.group.repository.GroupMemberRepository;
import com.app.skuthon.domain.group.repository.TeamGroupRepository;
import com.app.skuthon.domain.mission.entity.MissionStatus;
import com.app.skuthon.domain.point.entity.ReasonType;
import com.app.skuthon.domain.point.service.PointService;
import com.app.skuthon.domain.proof.dto.response.ProofFeedResponse;
import com.app.skuthon.domain.proof.dto.response.ProofResponse;
import com.app.skuthon.domain.proof.entity.DailyProof;
import com.app.skuthon.domain.proof.exception.ProofErrorCode;
import com.app.skuthon.domain.proof.repository.DailyProofRepository;
import com.app.skuthon.domain.settlement.service.SettlementService;
import com.app.skuthon.domain.user.entity.User;
import com.app.skuthon.domain.user.exception.UserErrorCode;
import com.app.skuthon.domain.user.repository.UserRepository;
import com.app.skuthon.global.exception.CustomException;
import com.app.skuthon.global.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProofService {

  private static final int PROOF_REWARD_POINT = 30;

  private final DailyProofRepository dailyProofRepository;
  private final GroupMemberRepository groupMemberRepository;
  private final TeamGroupRepository teamGroupRepository;
  private final UserRepository userRepository;
  private final FileStorageService fileStorageService;
  private final PointService pointService;
  private final SettlementService settlementService;

  /** 인증샷 업로드 (하루 1장) + 보상 30P */
  @Transactional
  public ProofResponse uploadProof(Long groupId, Long userId, MultipartFile photo) {
    LocalDate today = LocalDate.now();

    if (dailyProofRepository.existsByUserIdAndGroupIdAndProofDate(userId, groupId, today)) {
      throw new CustomException(ProofErrorCode.ALREADY_PROVED);
    }

    String photoUrl = fileStorageService.save(photo);

    DailyProof proof = DailyProof.builder()
        .userId(userId)
        .groupId(groupId)
        .proofDate(today)
        .photoUrl(photoUrl)
        .watermarkTime(LocalDateTime.now())
        .build();
    dailyProofRepository.save(proof);

    // 인증 보상 +30P
    pointService.addPoints(userId, PROOF_REWARD_POINT, ReasonType.PROOF_REWARD, proof.getId());

    settlementService.settleUser(groupId, userId, MissionStatus.SUCCESS);

    int remaining = userRepository.findById(userId).map(User::getPoints).orElse(0);
    return ProofResponse.of(proof, remaining);
  }

  /** 그룹 인증 피드: 멤버 전원 (미인증자는 photoUrl null) */
  @Transactional(readOnly = true)
  public List<ProofFeedResponse> getProofFeed(Long groupId, LocalDate date) {
    LocalDate targetDate = (date != null) ? date : LocalDate.now();

    TeamGroup group = teamGroupRepository.findById(groupId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    Map<Long, DailyProof> proofMap = dailyProofRepository
        .findByGroupIdAndProofDate(groupId, targetDate).stream()
        .collect(Collectors.toMap(DailyProof::getUserId, Function.identity()));

    return groupMemberRepository.findByTeamGroup(group).stream()
        .map(GroupMember::getUser)
        .map(user -> {
          DailyProof proof = proofMap.get(user.getId());
          return new ProofFeedResponse(
              user.getId(),
              user.getNickname(),
              proof != null ? proof.getPhotoUrl() : null,
              proof != null ? proof.getWatermarkTime() : null);
        })
        .toList();
  }
}