package com.app.skuthon.domain.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class MissionResponseDto {

    @Getter
    @AllArgsConstructor
    @Schema(description = "미션 등록 응답 DTO")
    public static class Create {
        @Schema(description = "생성된 미션 ID", example = "10")
        private Long missionId;
        @Schema(description = "미션 내용", example = "러닝머신 4KM 걷기")
        private String content;
        @Schema(description = "미션 상태", example = "PENDING")
        private String status;
    }

    @Getter
    @AllArgsConstructor
    @Schema(description = "내 미션 목록 응답 DTO")
    public static class MyList {
        @Schema(description = "미션 ID", example = "10")
        private Long missionId;
        @Schema(description = "미션 내용", example = "스쿼트 60회&런지200회")
        private String content;
        @Schema(description = "미션 상태", example = "PENDING")
        private String status;
    }

    @Getter
    @AllArgsConstructor
    @Schema(description = "베팅판 목록 응답 DTO")
    public static class BettingBoard {
        @Schema(description = "미션 ID", example = "10")
        private Long missionId;
        @Schema(description = "대상 유저 ID", example = "1")
        private Long targetUserId;
        @Schema(description = "대상 유저 닉네임", example = "승민")
        private String targetNickname;
        @Schema(description = "미션 내용", example = "미션1(스쿼트 60회&런지200회)")
        private String content;
        @Schema(description = "미션 상태", example = "PENDING")
        private String status;

        @Schema(description = "성공 베팅 총액", example = "200")
        private int successPool;
        @Schema(description = "실패 베팅 총액", example = "100")
        private int failPool;
        @Schema(description = "성공 배당률(%)", example = "60")
        private int successRate;
        @Schema(description = "실패 배당률(%)", example = "40")
        private int failRate;
        @Schema(description = "성공 베팅 인원", example = "3")
        private int successBettors;
        @Schema(description = "실패 베팅 인원", example = "2")
        private int failBettors;

        @Schema(description = "내 베팅 정보 (안 걸었으면 null)")
        private MyBetInfo myBet;
    }

    @Getter
    @AllArgsConstructor
    @Schema(description = "내 베팅 정보 DTO")
    public static class MyBetInfo {
        @Schema(description = "베팅 ID", example = "55")
        private Long betId;
        @Schema(description = "베팅 타입 (SUCCESS/FAIL)", example = "SUCCESS")
        private String betType;
        @Schema(description = "베팅한 금액", example = "100")
        private int betAmount;
    }
}