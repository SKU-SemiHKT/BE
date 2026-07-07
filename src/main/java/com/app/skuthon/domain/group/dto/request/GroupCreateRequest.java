package com.app.skuthon.domain.group.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Schema(description = "그룹 생성 요청 DTO")
public class GroupCreateRequest {

    @Schema(description = "요청하는 유저의 ID", example = "1")
    private Long userId;

    @Schema(description = "생성할 그룹의 이름", example = "어제의 나는 죽었다")
    private String name;

    @Schema(description = "그룹의 메인 목표", example = "11월에 다같이 바프찍기")
    private String mainGoal;

    @Schema(description = "마감일 (YYYY-MM-DD)", example = "2026-08-30")
    private LocalDate deadline;
}