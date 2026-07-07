package com.app.skuthon.domain.bet.controller;

import com.app.skuthon.domain.bet.dto.request.BetCreateRequest;
import com.app.skuthon.domain.bet.dto.response.BetResponse;
import com.app.skuthon.domain.bet.dto.response.MyBetHistoryResponse;
import com.app.skuthon.domain.bet.service.BetService;
import com.app.skuthon.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Bet", description = "베팅 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BetController {

  private final BetService betService;

  @Operation(summary = "베팅 걸기",
      description = "그룹의 특정 유저가 오늘 미션을 전부 수행할지(SUCCESS)/못할지(FAIL)에 포인트를 겁니다. "
          + "본인 베팅 불가, 10~100P, 유저당 하루 1회.")
  @PostMapping("/groups/{groupId}/users/{targetUserId}/bets")
  public ResponseEntity<BaseResponse<BetResponse>> placeBet(
      @Parameter(description = "그룹 식별자", example = "1") @PathVariable Long groupId,
      @Parameter(description = "베팅 대상 유저 식별자", example = "3") @PathVariable Long targetUserId,
      @RequestBody BetCreateRequest request) {
    BetResponse response = betService.placeBet(groupId, targetUserId, request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "베팅이 완료되었습니다.", response));
  }

  @Operation(summary = "베팅 취소",
      description = "정산 전(PENDING) 본인 베팅을 취소하고 포인트를 환불받습니다.")
  @DeleteMapping("/bets/{betId}")
  public ResponseEntity<BaseResponse<BetResponse>> cancelBet(
      @Parameter(description = "베팅 식별자", example = "55") @PathVariable Long betId,
      @Parameter(description = "베팅자 식별자", example = "1") @RequestParam Long bettorId) {
    return ResponseEntity.ok(BaseResponse.success("베팅이 취소되었습니다.",
        betService.cancelBet(betId, bettorId)));
  }

  @Operation(summary = "베팅 변경 (결과 재선택)",
      description = "정산 전 본인 베팅의 예측(성공/실패)과 금액을 변경합니다. 기존 금액 환불 후 새 금액 차감.")
  @PutMapping("/bets/{betId}")
  public ResponseEntity<BaseResponse<BetResponse>> changeBet(
      @Parameter(description = "베팅 식별자", example = "55") @PathVariable Long betId,
      @RequestBody BetCreateRequest request) {
    return ResponseEntity.ok(BaseResponse.success("베팅이 변경되었습니다.",
        betService.changeBet(betId, request)));
  }

  @Operation(summary = "내 베팅 내역",
      description = "내가 건 베팅들의 예측/결과/수익 목록 (최신순). "
          + "profit — WIN: 원금+배당, LOSE: -베팅액, PENDING: null(진행중)")
  @GetMapping("/users/{userId}/bets")
  public ResponseEntity<BaseResponse<List<MyBetHistoryResponse>>> getMyBetHistory(
      @Parameter(description = "유저 식별자", example = "1") @PathVariable Long userId) {
    return ResponseEntity.ok(BaseResponse.success(betService.getMyBetHistory(userId)));
  }
}