package com.app.skuthon.domain.group.dto.request;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public class GroupCreateRequest {
    private Long userId; // 방장 유저 ID (시연 시 주로 1이 들어옴)
    private String name; // 그룹명
    private String mainGoal; // 큰 목표
    private LocalDate deadline; // 마감 날짜
}