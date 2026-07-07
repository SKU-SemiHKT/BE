package com.app.skuthon.domain.group.controller;

import com.app.skuthon.domain.group.dto.request.GroupCreateRequest;
import com.app.skuthon.domain.group.dto.request.GroupJoinRequest;
import com.app.skuthon.domain.group.dto.response.GroupResponseDto;
import com.app.skuthon.domain.group.service.GroupService;
import com.app.skuthon.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Group", description = "그룹 관련 API")
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    // 2-1. 그룹 생성
    @PostMapping
    @Operation(summary = "그룹 생성", description = "그룹 생성 API - 새로운 그룹을 생성하고 6자리 랜덤 초대 코드를 발급")
    public ResponseEntity<BaseResponse<GroupResponseDto.Create>> createGroup(@RequestBody GroupCreateRequest request) {
        GroupResponseDto.Create response = groupService.createGroup(
                request.getUserId(), request.getName(), request.getMainGoal(), request.getDeadline()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(201, "그룹 생성 성공", response));
    }

    // 2-2. 그룹 참여
    @PostMapping("/join")
    @Operation(summary = "그룹 참여", description = "그룹 참여 API - 발급된 6자리 초대 코드를 입력하여 기존 그룹에 참여")
    public ResponseEntity<BaseResponse<GroupResponseDto.Join>> joinGroup(@RequestBody GroupJoinRequest request) {
        GroupResponseDto.Join response = groupService.joinGroup(request.getUserId(), request.getInviteCode());
        return ResponseEntity.ok(BaseResponse.success(200, "그룹 참여 성공", response));
    }

    // 2-3. 내 그룹 목록
    @GetMapping
    @Operation(summary = "내 그룹 목록 조회", description = "내 그룹 목록 조회 API - 특정 사용자가 속한 모든 그룹의 목록과 D-Day, 멤버 수 조회")
    public ResponseEntity<BaseResponse<List<GroupResponseDto.ListInfo>>> getMyGroups(@RequestParam Long userId) {
        List<GroupResponseDto.ListInfo> response = groupService.getMyGroups(userId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    // 2-4. 그룹 상세
    @GetMapping("/{groupId}")
    @Operation(summary = "그룹 상세 조회", description = "그룹 상세 조회 API - 특정 그룹의 목표, 마감일, 소속된 멤버 목록을 상세 조회")
    public ResponseEntity<BaseResponse<GroupResponseDto.Detail>> getGroupDetail(@PathVariable Long groupId) {
        GroupResponseDto.Detail response = groupService.getGroupDetail(groupId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}