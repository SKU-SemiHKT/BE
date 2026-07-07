package com.app.skuthon.domain.group.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "그룹 참여 요청 DTO")
public class GroupJoinRequest {

    @Schema(description = "참여하는 유저의 ID", example = "1")
    private Long userId;

    @Schema(description = "공유받은 6자리 초대 코드 (대문자+숫자)", example = "A1B2C3")
    private String inviteCode;
}