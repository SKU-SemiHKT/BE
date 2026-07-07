package com.app.skuthon.domain.group.controller;

import com.app.skuthon.domain.group.dto.request.GroupCreateRequest;
import com.app.skuthon.domain.group.dto.request.GroupJoinRequest;
import com.app.skuthon.domain.group.dto.response.GroupResponseDto;
import com.app.skuthon.domain.group.service.GroupService;
import com.app.skuthon.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Group", description = "그룹 관련 API")@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    // 2-1. 그룹 생성
    @PostMapping
    public ResponseEntity<BaseResponse<GroupResponseDto.Create>> createGroup(@RequestBody GroupCreateRequest request) {
        GroupResponseDto.Create response = groupService.createGroup(
                request.getUserId(), request.getName(), request.getMainGoal(), request.getDeadline()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(201, "그룹 생성 성공", response));
    }

    // 2-2. 그룹 참여
    @PostMapping("/join")
    public ResponseEntity<BaseResponse<GroupResponseDto.Join>> joinGroup(@RequestBody GroupJoinRequest request) {
        GroupResponseDto.Join response = groupService.joinGroup(request.getUserId(), request.getInviteCode());
        return ResponseEntity.ok(BaseResponse.success(200, "그룹 참여 성공", response));
    }

    // 2-3. 내 그룹 목록
    @GetMapping
    public ResponseEntity<BaseResponse<List<GroupResponseDto.ListInfo>>> getMyGroups(@RequestParam Long userId) {
        List<GroupResponseDto.ListInfo> response = groupService.getMyGroups(userId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    // 2-4. 그룹 상세
    @GetMapping("/{groupId}")
    public ResponseEntity<BaseResponse<GroupResponseDto.Detail>> getGroupDetail(@PathVariable Long groupId) {
        GroupResponseDto.Detail response = groupService.getGroupDetail(groupId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}