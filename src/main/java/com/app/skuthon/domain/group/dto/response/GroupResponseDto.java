package com.app.skuthon.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

public class GroupResponseDto {

    // 2-1. 그룹 생성 응답
    @Getter @AllArgsConstructor
    public static class Create {
        private Long groupId;
        private String name;
        private String mainGoal;
        private LocalDate deadline;
        private String inviteCode;
    }

    // 2-2. 그룹 참여 응답
    @Getter @AllArgsConstructor
    public static class Join {
        private Long groupId;
        private String name;
    }

    // 2-3. 내 그룹 목록 응답
    @Getter @AllArgsConstructor
    public static class ListInfo {
        private Long groupId;
        private String name;
        private String mainGoal;
        private LocalDate deadline;
        private int memberCount;
        private long dDay;
    }

    // 2-4. 그룹 상세 응답
    @Getter @AllArgsConstructor
    public static class Detail {
        private Long groupId;
        private String name;
        private String mainGoal;
        private LocalDate deadline;
        private long dDay;
        private List<MemberInfo> members;
    }

    @Getter @AllArgsConstructor
    public static class MemberInfo {
        private Long userId;
        private String nickname;
    }
}