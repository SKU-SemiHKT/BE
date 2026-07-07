package com.app.skuthon.domain.mission.controller;

import com.app.skuthon.domain.mission.dto.request.MissionRequestDto;
import com.app.skuthon.domain.mission.dto.response.MissionResponseDto;
import com.app.skuthon.domain.mission.service.MissionService;
import com.app.skuthon.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Mission", description = "개인 미션 및 베팅판 관련 API")
@RestController
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // 3-1. 미션 등록
    @PostMapping("/api/missions")
    @Operation(summary = "미션 등록", description = "미션 등록 API - 하루에 여러 개의 개인 미션을 추가할 수 있습니다.")
    public ResponseEntity<BaseResponse<MissionResponseDto.Create>> createMission(
            @RequestBody MissionRequestDto.Create request) {

        MissionResponseDto.Create response = missionService.createMission(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(201, "미션 등록 성공", response));
    }

    // 3-2. 내 미션 목록 조회
    @GetMapping("/api/groups/{groupId}/missions/my")
    @Operation(summary = "내 미션 목록 조회", description = "내 미션 목록 조회 API - 홈 화면 체크리스트 용도. 당일 자정 기준 남은 시간은 프론트에서 계산합니다.")
    public ResponseEntity<BaseResponse<List<MissionResponseDto.MyList>>> getMyMissions(
            @PathVariable Long groupId,
            @RequestParam Long userId) {

        List<MissionResponseDto.MyList> response = missionService.getMyMissions(groupId, userId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    // 3-3. 베팅판 목록 조회
    @GetMapping("/api/groups/{groupId}/betting-board")
    @Operation(summary = "베팅판 목록 조회", description = "베팅판 목록 조회 API - 그룹원들의 미션 카드(본인 제외)와 배당률, 내 베팅 내역을 함께 조회합니다.")
    public ResponseEntity<BaseResponse<List<MissionResponseDto.BettingBoard>>> getBettingBoard(
            @PathVariable Long groupId,
            @RequestParam Long userId) {

        List<MissionResponseDto.BettingBoard> response = missionService.getBettingBoard(groupId, userId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}