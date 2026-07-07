package com.app.skuthon.domain.proof.controller;

import com.app.skuthon.domain.proof.dto.response.ProofFeedResponse;
import com.app.skuthon.domain.proof.dto.response.ProofResponse;
import com.app.skuthon.domain.proof.service.ProofService;
import com.app.skuthon.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Proof", description = "인증샷 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProofController {

  private final ProofService proofService;

  @Operation(summary = "인증샷 업로드",
      description = "오늘의 인증 사진을 업로드합니다. 하루 1장, 업로드 즉시 +30P 지급. "
          + "정산 시 이 인증 유무로 그날 미션 전체 성공/실패가 판정됩니다.")
  @PostMapping(value = "/groups/{groupId}/proofs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BaseResponse<ProofResponse>> uploadProof(
      @Parameter(description = "그룹 식별자", example = "1") @PathVariable Long groupId,
      @Parameter(description = "인증하는 유저 식별자", example = "1") @RequestParam Long userId,
      @Parameter(description = "인증 사진 파일") @RequestPart MultipartFile photo) {
    ProofResponse response = proofService.uploadProof(groupId, userId, photo);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "인증이 완료되었습니다. +30P", response));
  }

  @Operation(summary = "그룹 인증 피드",
      description = "그룹 멤버 전원의 오늘(또는 지정 날짜) 인증 현황. 미인증자는 photoUrl이 null.")
  @GetMapping("/groups/{groupId}/proofs")
  public ResponseEntity<BaseResponse<List<ProofFeedResponse>>> getProofFeed(
      @Parameter(description = "그룹 식별자", example = "1") @PathVariable Long groupId,
      @Parameter(description = "조회 날짜 (생략 시 오늘)", example = "2026-07-07")
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.ok(BaseResponse.success(proofService.getProofFeed(groupId, date)));
  }
}
