package com.app.skuthon.domain.settlement.controller;

import com.app.skuthon.domain.settlement.dto.response.SettlementResultResponse;
import com.app.skuthon.domain.settlement.service.SettlementService;
import com.app.skuthon.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Settlement", description = "정산 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SettlementController {

  private final SettlementService settlementService;

  @Operation(summary = "미인증자 실패 정산 (수동 트리거)",
      description = "그룹에서 오늘 인증하지 않은 멤버들을 일괄 FAIL 처리하고 베팅을 정산합니다. "
          + "실서비스에서는 매일 자정 스케줄러가 동일 로직을 수행합니다. (인증자는 업로드 시 이미 정산됨)")
  @PostMapping("/groups/{groupId}/settle")
  public ResponseEntity<BaseResponse<List<SettlementResultResponse>>> settleFailures(
      @Parameter(description = "그룹 식별자", example = "1") @PathVariable Long groupId) {
    return ResponseEntity.ok(BaseResponse.success("정산이 완료되었습니다.",
        settlementService.settleFailures(groupId)));
  }
}