package com.app.skuthon.domain.group.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

public class GroupResponseDto {

    @Getter
    @AllArgsConstructor
    @Schema(description = "그룹 생성 응답 DTO")
    public static class Create {
        @Schema(description = "생성된 그룹 ID", example = "10")
        private Long groupId;
        @Schema(description = "그룹 이름", example = "어제의 나는 죽었다")
        private String name;
        @Schema(description = "메인 목표", example = "11월에 다같이 바프찍기")
        private String mainGoal;
        @Schema(description = "마감일", example = "2026-08-30")
        private LocalDate deadline;
        @Schema(description = "발급된 초대 코드", example = "X9Y8Z7")
        private String inviteCode;
    }

    @Getter
    @AllArgsConstructor
    @Schema(description = "그룹 참여 응답 DTO")
    public static class Join {
        @Schema(description = "참여한 그룹 ID", example = "10")
        private Long groupId;
        @Schema(description = "참여한 그룹 이름", example = "어제의 나는 죽었다")
        private String name;
    }

    @Getter
    @AllArgsConstructor
    @Schema(description = "내 그룹 목록 정보 DTO")
    public static class ListInfo {
        @Schema(description = "그룹 ID", example = "10")
        private Long groupId;
        @Schema(description = "그룹 이름", example = "어제의 나는 죽었다")
        private String name;
        @Schema(description = "메인 목표", example = "11월에 다같이 바프찍기")
        private String mainGoal;
        @Schema(description = "마감일", example = "2026-08-30")
        private LocalDate deadline;
        @Schema(description = "현재 소속된 멤버 수", example = "4")
        private int memberCount;
        @Schema(description = "마감일까지 남은 D-Day", example = "15")
        private long dDay;
    }

    @Getter
    @AllArgsConstructor
    @Schema(description = "그룹 상세 정보 DTO")
    public static class Detail {
        @Schema(description = "그룹 ID", example = "10")
        private Long groupId;
        @Schema(description = "그룹 이름", example = "어제의 나는 죽었다")
        private String name;
        @Schema(description = "메인 목표", example = "11월에 다같이 바프찍기")
        private String mainGoal;
        @Schema(description = "마감일", example = "2026-08-30")
        private LocalDate deadline;
        @Schema(description = "마감일까지 남은 D-Day", example = "15")
        private long dDay;
        @Schema(description = "소속 멤버 목록")
        private List<MemberInfo> members;
    }

    @Getter
    @AllArgsConstructor
    @Schema(description = "그룹 소속 멤버 정보 DTO")
    public static class MemberInfo {
        @Schema(description = "유저 ID", example = "1")
        private Long userId;
        @Schema(description = "유저 닉네임", example = "주희")
        private String nickname;
    }
}