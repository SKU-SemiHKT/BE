package com.app.skuthon.domain.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

public class MissionRequestDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "미션 등록 요청 DTO")
    public static class Create {
        @Schema(description = "유저 ID", example = "1")
        private Long userId;

        @Schema(description = "그룹 ID", example = "1")
        private Long groupId;

        @Schema(description = "미션 내용", example = "러닝머신 4KM 걷기")
        private String content;

    }
}